package com.jstik.fancy.user.service;

import com.jstik.fancy.user.dao.repository.*;
import com.jstik.fancy.user.entity.User;
import com.jstik.fancy.user.entity.UserOperationTimestamp;
import com.jstik.fancy.user.entity.UserRegistration;
import com.jstik.fancy.user.model.CreateAccountRequest;
import com.jstik.site.cassandra.model.EntityOperation;
import com.jstik.site.cassandra.statements.DMLStatementProducerBuilder;
import com.jstik.site.cassandra.statements.EntityAwareBatchStatement;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.inject.Inject;

import java.util.UUID;

import static com.jstik.site.cassandra.statements.DMLStatementProducerBuilder.insertStatementProducer;



public class UserService {

    private final ConversionService conversionService;

    private final UserRepository userRepository;

    @Inject
    public UserService(ConversionService conversionService, UserRepository userRepository) {
        this.conversionService = conversionService;
        this.userRepository = userRepository;
    }

    public  Mono<User> createUser(CreateAccountRequest account ,String regKey){
        User user = conversionService.convert(account, User.class);
        //String regKey = generateRegKey();

       return userRepository.insertIfNotExistOrThrow(user)
                .doOnSuccess(createdUser->{
                    afterUserCreate(user, regKey).block();
                });

    }

    private  Mono<Boolean> afterUserCreate(User user, String regKey){
        UserRegistration registration = new UserRegistration(user.getLogin(), regKey);
        UserOperationTimestamp timestamp = new UserOperationTimestamp(user.getLogin(), EntityOperation.CREATE);
        EntityAwareBatchStatement insertStatement = new EntityAwareBatchStatement(insertStatementProducer(false), registration);
        insertStatement =  insertStatement.andThen( new EntityAwareBatchStatement(insertStatementProducer(false), timestamp));
        return userRepository.executeBatch(insertStatement);
    }

    private  static  String generateRegKey(){
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
