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
import org.springframework.boot.web.servlet.support.ServletContextApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import javax.inject.Inject;

import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.*;

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

    private @Inject
    UserService userService;


    @Test
    public void createUserTest() throws Exception {
        CreateAccountRequest accountRequest = prepareAccount("login");
        String regKey = UserUtil.generateRegKey();
        Mono<User> createUserPublisher = userService.createUser(accountRequest, regKey);
        createUserPublisher.log("UserServiceTest.createUserTest").subscribe(Assert::assertNotNull);
    }


    @Test(expected = EntityAlreadyExistsException.class)
    public void createExistingUserTest() throws Exception {
        CountDownLatch startLatch = new CountDownLatch(2);
        CreateAccountRequest accountRequest = prepareAccount("login");
        String regKey = UserUtil.generateRegKey();

        Mono<User> firstUserPublisher = userService.createUser(accountRequest, regKey).doOnError(error -> {
            startLatch.countDown();
            if (error instanceof EntityAlreadyExistsException)
                throw (EntityAlreadyExistsException) error;
            throw new RuntimeException(error);
        }).doOnSuccess(user -> {
            startLatch.countDown();
            System.out.println("first");
        });


        Mono<User> secondUserPublisher = userService.createUser(accountRequest, regKey).doOnError(error -> {
            startLatch.countDown();
            if (error instanceof EntityAlreadyExistsException)
                throw (EntityAlreadyExistsException) error;
            throw new RuntimeException(error);
        }).doOnSuccess(user -> {
            startLatch.countDown();
            System.out.println("second");
        });
        Mono<User> composite = firstUserPublisher.delayUntil(user -> secondUserPublisher);
        // StepVerifier.create(composite).expectComplete();
        composite.block();
        startLatch.await();
    }

    @Test
    public void registerAccountTest() {

        RegisterAccountRequest registerAccount = new RegisterAccountRequest("login", "passord", "123");
        userService.registerAccount(registerAccount).subscribe(register -> {
            System.out.println("saddfsd");
        });
    }

    public CreateAccountRequest prepareAccount(String login) {
        CreateAccountRequest accountRequest = new CreateAccountRequest();
        accountRequest.setLogin(login);
        accountRequest.setFirstName("first name");
        accountRequest.setLastName("last name");
        accountRequest.setEmail("email");
        return accountRequest;
    }

}