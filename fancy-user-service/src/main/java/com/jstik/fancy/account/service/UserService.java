package com.jstik.fancy.account.service;

import com.jstik.fancy.account.dao.repository.TagRepository;
import com.jstik.fancy.account.dao.repository.UserRegistrationRepository;
import com.jstik.fancy.account.dao.repository.UserRepository;
import com.jstik.fancy.account.entity.client.UsersByClient;
import com.jstik.fancy.account.entity.tag.UserByTag;
import com.jstik.fancy.account.entity.user.*;
import com.jstik.fancy.account.exception.UserNotFound;
import com.jstik.fancy.account.model.AuthorityDTO;
import com.jstik.site.cassandra.model.EntityOperation;
import com.jstik.site.cassandra.statements.EntityAwareBatchStatement;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Mono;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;

import java.net.URI;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static com.jstik.site.cassandra.statements.DMLStatementProducerBuilder.insertProducer;

public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private UserRegistrationRepository userRegistrationRepository;
    private TagRepository tagRepository;

    private LoadBalancerClient loadBalancerClient;

    @Value("${spring.application.name}")
    private String applicationName;


    @Inject
    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       UserRegistrationRepository userRegistrationRepository,
                       TagRepository tagRepository,LoadBalancerClient loadBalancerClient
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRegistrationRepository = userRegistrationRepository;
        this.tagRepository = tagRepository;
        this.loadBalancerClient = loadBalancerClient;
    }

    public Mono<User> createUser(User user, String regKey) {
        UserRegistration registration = new UserRegistration(user.getLogin(), regKey);
        return userRepository.insertIfNotExistOrThrow(user)
                .delayUntil(inserted -> userRegistrationRepository.save(registration))
                .doOnSuccess(inserted -> {

                    insertBrandNewUserLinkedInBatch(inserted).subscribe();
                    if (user.getTags() != null && !user.getTags().isEmpty())
                        tagRepository.saveTags(user.getTags()).subscribe();
                });
    }


    public String regKeyToRegistrationUrl(String regKey){
        ServiceInstance serviceInstance = loadBalancerClient.choose(applicationName);
        URI uri = serviceInstance.getUri();
        String accountActivationUrl = serviceInstance.getMetadata().get("account.activation.url");
        return uri.toString();
    }

    public <T> Mono<User> saveUserAndDelayUntil(User user, Mono<T> until) {
        return userRepository.save(user).delayUntil(savedUser -> until);
    }

    public Mono<User> getUserToActivate(String login, String password) {
        return userRepository.findByPrimaryKeyLogin(login)
                .doOnSuccess(user -> {
                    if (user == null)
                        throw new UserNotFound();
                    user.setActive(true);
                    user.setPassword(passwordEncoder.encode(password));
                });
    }


    Mono<Boolean> insertBrandNewUserLinkedInBatch(User user) {
        EntityAwareBatchStatement batch = Stream.of(
                userRepository.userOperationStatement(user, EntityOperation.CREATE).orElse(null),
                userRepository.userAuthority(user.getAuthorities()).orElse(null),
                userRepository.userByTagStatement(user, user.getTags()).orElse(null),
                userRepository.usersByClientStatement(user, user.getClients()).orElse(null)
        ).filter(Objects::nonNull).reduce(EntityAwareBatchStatement::andThen).orElse(null);
        return batch != null ? userRepository.executeBatch(batch) : Mono.just(true);
    }
}
