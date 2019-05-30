package com.jstik.fancy.user.service;

import com.jstik.fancy.user.dao.repository.*;
import com.jstik.fancy.user.entity.User;
import com.jstik.fancy.user.entity.UserOperationTimestamp;
import com.jstik.fancy.user.entity.UserRegistration;
import com.jstik.fancy.user.entity.UserRegistration.UserRegistrationPrimaryKey;
import com.jstik.fancy.user.model.CreateAccountRequest;
import com.jstik.fancy.user.model.RegisterAccountRequest;
import com.jstik.site.cassandra.model.EntityOperation;
import com.jstik.site.cassandra.statements.EntityAwareBatchStatement;
import org.springframework.core.convert.ConversionService;
import org.springframework.web.server.NotAcceptableStatusException;
import reactor.core.publisher.Mono;
import javax.inject.Inject;

import static com.jstik.site.cassandra.statements.DMLStatementProducerBuilder.insertProducer;


public class UserService {

    private final ConversionService conversionService;

    private final UserRepository userRepository;

    private final UserRegistrationRepository userRegistrationRepository;

    @Inject
    public UserService(ConversionService conversionService, UserRepository userRepository,UserRegistrationRepository userRegistrationRepository) {
        this.conversionService = conversionService;
        this.userRepository = userRepository;
        this.userRegistrationRepository = userRegistrationRepository;
    }

    public Mono<User> createUser(CreateAccountRequest account, String regKey) {
        User user = conversionService.convert(account, User.class);
        return userRepository.insertIfNotExistOrThrow(user).log("UserService.createUser.").doOnSuccess(createdUser -> {
            afterUserCreate(user, regKey).subscribe();
        });
    }

    public  Mono<User> registerAccount(RegisterAccountRequest registerAccount){

        UserRegistrationPrimaryKey registrationId = new UserRegistrationPrimaryKey(registerAccount.getLogin(), registerAccount.getRegKey());
        Mono<UserRegistration> findUserRegistrationOperation = userRegistrationRepository.findById(registrationId);

        Mono<User> findUserOperation = userRepository.findById(registerAccount.getLogin());

        /*findUserOperation.delayUntil(user -> {
           return findUserRegistrationOperation;
        });*/

        findUserRegistrationOperation.subscribe();

        return findUserOperation.doOnSuccess(user->{
            if(user == null)
                throw new NotAcceptableStatusException("User not exists");

        });
    }



    private Mono<Boolean> afterUserCreate(User user, String regKey) {
        UserRegistration registration = new UserRegistration(user.getLogin(), regKey);
        UserOperationTimestamp timestamp = new UserOperationTimestamp(user.getLogin(), EntityOperation.CREATE);
        EntityAwareBatchStatement insertStatement = new EntityAwareBatchStatement(insertProducer(), registration);
        insertStatement = insertStatement.andThen(new EntityAwareBatchStatement(insertProducer(), timestamp));
        return userRepository.executeBatch(insertStatement);
    }
}
