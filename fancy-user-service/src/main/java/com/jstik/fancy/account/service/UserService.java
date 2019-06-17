package com.jstik.fancy.account.service;

import com.jstik.fancy.account.dao.repository.UserRepository;
import com.jstik.fancy.account.entity.user.User;
import com.jstik.fancy.account.entity.user.UserOperationTimestamp;
import com.jstik.fancy.account.entity.user.UserOperations;
import com.jstik.fancy.account.entity.user.UserRegistration;
import com.jstik.fancy.account.exception.UserNotFound;
import com.jstik.site.cassandra.model.EntityOperation;
import com.jstik.site.cassandra.statements.EntityAwareBatchStatement;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Mono;

import javax.inject.Inject;

import static com.jstik.site.cassandra.statements.DMLStatementProducerBuilder.insertProducer;

public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Inject
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Mono<User> createUser(User user, String regKey) {
        return userRepository.insertIfNotExistOrThrow(user).log("UserService.createAccount.").doOnSuccess(createdUser -> {
            insertBrandNewUserLinkedInBatch(user, regKey).subscribe();
        });
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


    Mono<Boolean> insertBrandNewUserLinkedInBatch(User user, String regKey) {
        UserRegistration registration = new UserRegistration(user.getLogin(), regKey);
        UserOperations timestamp = new UserOperations(user, EntityOperation.CREATE);
        EntityAwareBatchStatement insertStatement = new EntityAwareBatchStatement(insertProducer(), registration);
        insertStatement = insertStatement.andThen(new EntityAwareBatchStatement(insertProducer(), timestamp));
        return userRepository.executeBatch(insertStatement);
    }
}
