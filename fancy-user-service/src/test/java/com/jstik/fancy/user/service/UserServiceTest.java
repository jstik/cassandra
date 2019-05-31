package com.jstik.fancy.user.service;

import com.jstik.fancy.test.util.cassandra.EmbeddedCassandraConfig;
import com.jstik.fancy.test.util.cassandra.EmbeddedCassandraEnvironment;
import com.jstik.fancy.user.dao.UserServiceCassandraConfig;
import com.jstik.fancy.user.entity.User;
import com.jstik.fancy.user.model.CreateAccountRequest;
import com.jstik.fancy.user.model.RegisterAccountRequest;
import com.jstik.fancy.user.util.UserUtil;
import com.jstik.fancy.user.web.UserServiceWebConfig;
import com.jstik.site.cassandra.exception.EntityAlreadyExistsException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import javax.inject.Inject;
import java.util.function.Consumer;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringJUnitWebConfig
@ContextConfiguration(
        classes = {
                EmbeddedCassandraConfig.class, UserServiceCassandraConfig.class,
                ServiceConfig.class, UserServiceWebConfig.class
        }
)
@TestPropertySource("classpath:embedded-test.properties")
public class UserServiceTest extends EmbeddedCassandraEnvironment {

    @Inject private UserService userService;

    private  final Logger log = LoggerFactory.getLogger(this.getClass());


    @Test
    public void createUserTest() throws Exception {
        CreateAccountRequest accountRequest = prepareAccount("login");
        String regKey = UserUtil.generateRegKey();
        Mono<User> createUserPublisher = userService.createUser(accountRequest, regKey);
        createUserPublisher.log("UserServiceTest.createUserTest").subscribe(Assert::assertNotNull);
    }


    @Test(expected = EntityAlreadyExistsException.class)
    public void createExistingUserBlockTest() throws Exception {
        CreateAccountRequest account = prepareAccount("login");
        String regKey = UserUtil.generateRegKey();
        Consumer<Throwable> errorHandler = (error) -> {
            if (error instanceof EntityAlreadyExistsException)
                throw (EntityAlreadyExistsException) error;
            throw new RuntimeException(error);
        };
        Mono<User> firstUserPublisher = createUserOperation(account, regKey, user ->  log.debug("First"), errorHandler);
        Mono<User> secondUserPublisher = createUserOperation(account, regKey, user ->  log.debug("Second") , errorHandler);
        Mono<User> composite = firstUserPublisher.delayUntil(user -> secondUserPublisher);
        composite.block();
    }

    @Test
    public void createExistingUserTest(){
        CreateAccountRequest account = prepareAccount("login");
        String regKey = UserUtil.generateRegKey();
        Consumer<Throwable> errorHandler = (error) -> {
            if (error instanceof EntityAlreadyExistsException)
                throw (EntityAlreadyExistsException) error;
            throw new RuntimeException(error);
        };
        Mono<User> firstUserPublisher = createUserOperation(account, regKey, user ->  log.debug("First"), errorHandler);
        Mono<User> secondUserPublisher = createUserOperation(account, regKey, user ->  log.debug("Second") , errorHandler);
        Mono<User> composite = firstUserPublisher.delayUntil(user -> secondUserPublisher);
        StepVerifier.create(composite).expectError(EntityAlreadyExistsException.class).verify();
    }

    @Test
    public void registerAccountTest() {
        RegisterAccountRequest registerAccount = new RegisterAccountRequest("login", "passord", "123");
        userService.registerAccount(registerAccount).subscribe(register -> {
            System.out.println("saddfsd");
        });
    }

    private Mono<User> createUserOperation(CreateAccountRequest accountRequest,   String regKey , Consumer<User> onSuccess,Consumer<? super Throwable> onError){
      return userService.createUser(accountRequest, regKey) .doOnError(onError::accept).doOnSuccess(onSuccess);
    }

    private CreateAccountRequest prepareAccount(String login) {
        CreateAccountRequest accountRequest = new CreateAccountRequest();
        accountRequest.setLogin(login);
        accountRequest.setFirstName("first name");
        accountRequest.setLastName("last name");
        accountRequest.setEmail("email");
        return accountRequest;
    }

}