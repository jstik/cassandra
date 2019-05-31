package com.jstik.fancy.user.service;

import com.jstik.fancy.user.dao.repository.*;
import com.jstik.fancy.user.entity.User;
import com.jstik.fancy.user.entity.UserOperationTimestamp;
import com.jstik.fancy.user.entity.UserRegistration;
import com.jstik.fancy.user.entity.UserRegistration.UserRegistrationPrimaryKey;
import com.jstik.fancy.user.exception.UserNotFound;
import com.jstik.fancy.user.exception.UserRegistrationNoFound;
import com.jstik.fancy.user.model.account.CreateAccountRequest;
import com.jstik.fancy.user.model.account.RegisterAccountRequest;
import com.jstik.fancy.user.model.account.RegisterAccountRequiredInfo;
import com.jstik.site.cassandra.model.EntityOperation;
import com.jstik.site.cassandra.statements.EntityAwareBatchStatement;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import javax.inject.Inject;

import static com.jstik.site.cassandra.statements.DMLStatementProducerBuilder.insertProducer;


public class UserService {

    private final ConversionService conversionService;

    private final UserRepository userRepository;

    private final UserRegistrationRepository userRegistrationRepository;

    private final PasswordEncoder passwordEncoder;

    @Inject
    public UserService(ConversionService conversionService, UserRepository userRepository,UserRegistrationRepository userRegistrationRepository, PasswordEncoder passwordEncoder) {
        this.conversionService = conversionService;
        this.userRepository = userRepository;
        this.userRegistrationRepository = userRegistrationRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Mono<User> createUser(CreateAccountRequest account, String regKey) {
        User user = conversionService.convert(account, User.class);
        return userRepository.insertIfNotExistOrThrow(user).log("UserService.createUser.").doOnSuccess(createdUser -> {
            afterUserCreate(user, regKey).subscribe();
        });
    }

    public Mono<RegisterAccountRequiredInfo>  registerAccount(RegisterAccountRequest registerAccount){
        Mono<RegisterAccountRequiredInfo> registrationInformation = findRequiredRegistrationInformation(registerAccount);
        registrationInformation.doOnSuccess((info-> doRegisterUser(info.getUserRegistration(), info.getUser()).subscribe()));
        return registrationInformation;
    }


    private Mono<RegisterAccountRequiredInfo> findRequiredRegistrationInformation(RegisterAccountRequest registerAccount){
        UserRegistrationPrimaryKey registrationId = new UserRegistrationPrimaryKey(registerAccount.getLogin(), registerAccount.getRegKey());
        Mono<UserRegistration> findRegistrationOperation = userRegistrationRepository.findById(registrationId)
                .doOnSuccess(registration -> {
                    if(registration == null)
                        throw new UserRegistrationNoFound();
                });

        Mono<User> findUserOperation = userRepository.findById(registerAccount.getLogin())
                .doOnSuccess(user-> {
                    if(user == null)
                        throw new UserNotFound();
                    user.setActive(true);
                    user.setPassword(passwordEncoder.encode(registerAccount.getPassword()));
                });
       return Mono.zip(findRegistrationOperation, findUserOperation , (reg, user) -> new RegisterAccountRequiredInfo(user, reg));
    }


    private Mono<User>  doRegisterUser(UserRegistration userRegistration, User user){
       return userRepository.save(user).delayUntil(savedUser->  userRegistrationRepository.delete(userRegistration));
    }


    private Mono<Boolean> afterUserCreate(User user, String regKey) {
        UserRegistration registration = new UserRegistration(user.getLogin(), regKey);
        UserOperationTimestamp timestamp = new UserOperationTimestamp(user.getLogin(), EntityOperation.CREATE);
        EntityAwareBatchStatement insertStatement = new EntityAwareBatchStatement(insertProducer(), registration);
        insertStatement = insertStatement.andThen(new EntityAwareBatchStatement(insertProducer(), timestamp));
        return userRepository.executeBatch(insertStatement);
    }
}
