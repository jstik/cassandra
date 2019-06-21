package com.jstik.fancy.account.service;

import com.jstik.fancy.account.dao.repository.TagRepository;
import com.jstik.fancy.account.dao.repository.UserOperationsRepository;
import com.jstik.fancy.account.dao.repository.UserRepository;
import com.jstik.fancy.account.entity.user.*;
import com.jstik.fancy.account.exception.UserNotFound;
import com.jstik.fancy.account.model.account.NewUserInfo;
import com.jstik.site.cassandra.statements.EntityAwareBatchStatement;
import reactor.core.publisher.Mono;

import javax.inject.Inject;

import java.util.Objects;
import java.util.stream.Stream;

import static com.jstik.site.cassandra.model.EntityOperation.CREATE;
import static com.jstik.site.cassandra.model.EntityOperation.UPDATE;

public class UserService {

    private final UserRepository userRepository;

    private final UserRegistrationService userRegistrationService;
    private UserOperationsRepository operationsRepository;
    private TagRepository tagRepository;


    @Inject
    public UserService(UserRepository userRepository,
                       UserRegistrationService userRegistrationService,
                       UserOperationsRepository operationsRepository,
                       TagRepository tagRepository
    ) {
        this.userRepository = userRepository;
        this.operationsRepository = operationsRepository;
        this.tagRepository = tagRepository;
        this.userRegistrationService = userRegistrationService;
    }

    public Mono<NewUserInfo> createUser(User user, String regKey) {
        Mono<NewUserInfo> result = Mono.just(new NewUserInfo());

        result = result.delayUntil(info -> insertUser(user).doOnSuccess(inserted -> info.setUser(user)));

        result = result.delayUntil(info -> {
            String login = info.getUser().getLogin();
            return userRegistrationService.createRegistration(login, regKey)
                    .doOnSuccess(reg -> info.setRegKey(reg.getRegKey()));
        });

        return result.doOnSuccess(info -> {
            insertBrandNewUserLinkedInBatch(info.getUser()).doOnSuccess(info::setLinkedInserted).subscribe();
            if (info.getUser().getTags() != null && !info.getUser().getTags().isEmpty())
                tagRepository.saveTags(info.getUser().getTags()).subscribe();
        });
    }


    public Mono<User> findUserOrThrow(String login) {
        return userRepository.findByPrimaryKeyLogin(login).switchIfEmpty(Mono.error(new UserNotFound()));
    }

    public Mono<User> activateUser(User user, String password) {
        user.setActive(true);
        user.setPassword(userRegistrationService.encodePassword(password));
        return updateUser(user);
    }

    Mono<User> updateUser(User user) {
        return userRepository.save(user).doOnSuccess(updated -> operationsRepository.save(new UserOperations(updated, UPDATE)).subscribe());
    }

    Mono<User> insertUser(User user) {
        return userRepository.insertIfNotExistOrThrow(user).doOnSuccess(created -> {
            UserOperations operation = new UserOperations(created, CREATE);
            operationsRepository.save(operation).subscribe();
        });
    }


    Mono<Boolean> insertBrandNewUserLinkedInBatch(User user) {
        EntityAwareBatchStatement batch = Stream.of(
                userRepository.userAuthority(user.getAuthorities()).orElse(null),
                userRepository.userByTagStatement(user, user.getTags()).orElse(null),
                userRepository.usersByClientStatement(user, user.getClients()).orElse(null)
        ).filter(Objects::nonNull).reduce(EntityAwareBatchStatement::andThen).orElse(null);
        return batch != null ? userRepository.executeBatch(batch) : Mono.just(true);
    }
}
