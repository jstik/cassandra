package com.jstik.fancy.account.service;

import com.jstik.fancy.account.dao.repository.UserRegistrationRepository;
import com.jstik.fancy.account.entity.cassandra.user.UserRegistration;
import com.jstik.fancy.account.entity.cassandra.user.UserRegistration.UserRegistrationPrimaryKey;
import com.jstik.fancy.account.exception.UserRegistrationNoFound;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import javax.inject.Inject;
import java.net.URI;

public class UserRegistrationService {

    private final UserRegistrationRepository userRegistrationRepository;
    private final PasswordEncoder passwordEncoder;
    private final LoadBalancerClient loadBalancerClient;

    @Value("${spring.application.name}")
    private String applicationName;


    @Inject
    public UserRegistrationService(UserRegistrationRepository userRegistrationRepository,
                                   PasswordEncoder passwordEncoder,
                                   LoadBalancerClient loadBalancerClient) {
        this.userRegistrationRepository = userRegistrationRepository;
        this.passwordEncoder = passwordEncoder;
        this.loadBalancerClient = loadBalancerClient;
    }

    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    public Mono<UserRegistration> createRegistration(String login, String regKey) {
        UserRegistration registration = new UserRegistration(login, regKey);
        return userRegistrationRepository.save(registration);
    }

    public Mono<UserRegistration> findRegistration(String login, String regKey) {
        UserRegistrationPrimaryKey primaryKey = new UserRegistrationPrimaryKey(login, regKey);
        return userRegistrationRepository.findById(primaryKey);
    }

    public Mono<UserRegistration> findRegistrationOrThrow(String login, String regKey) {
        UserRegistrationPrimaryKey primaryKey = new UserRegistrationPrimaryKey(login, regKey);
        return userRegistrationRepository.findById(primaryKey).switchIfEmpty(Mono.error(new UserRegistrationNoFound()));
    }

    public Mono<Void> delete(String login, String regKey){
        UserRegistrationPrimaryKey primaryKey = new UserRegistrationPrimaryKey(login, regKey);
        return userRegistrationRepository.deleteById(primaryKey);
    }


    //X-Forwarded-Host
    public String regKeyToRegistrationUrl(String login, String regKey) {
        ServiceInstance serviceInstance = loadBalancerClient.choose(applicationName);
        URI uri = serviceInstance.getUri();
        String accountActivationUrl = serviceInstance.getMetadata().get("account.activation.url");
        return UriComponentsBuilder.fromUri(uri).path(accountActivationUrl).buildAndExpand(login, regKey).toUriString();
    }

}
