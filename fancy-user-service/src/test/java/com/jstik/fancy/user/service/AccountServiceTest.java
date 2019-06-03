package com.jstik.fancy.user.service;

import com.jstik.fancy.test.util.cassandra.EmbeddedCassandraConfig;
import com.jstik.fancy.test.util.cassandra.EmbeddedCassandraEnvironment;
import com.jstik.fancy.user.dao.UserServiceCassandraConfig;
import com.jstik.fancy.user.dao.repository.UserRegistrationRepository;
import com.jstik.fancy.user.dao.repository.UserRepository;
import com.jstik.fancy.user.entity.User;
import com.jstik.fancy.user.entity.UserRegistration;
import com.jstik.fancy.user.exception.EntityMissingException;
import com.jstik.fancy.user.exception.UserNotFound;
import com.jstik.fancy.user.exception.UserRegistrationNoFound;
import com.jstik.fancy.user.model.account.CreateAccountRequest;
import com.jstik.fancy.user.model.account.RegisterAccountRequest;
import com.jstik.fancy.user.model.account.ActivateAccountRequiredInfo;
import com.jstik.fancy.user.security.UserServiceSecurityConfig;
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
                ServiceConfig.class, UserServiceWebConfig.class,
                UserServiceSecurityConfig.class
        }
)
@TestPropertySource("classpath:embedded-test.properties")
public class AccountServiceTest extends EmbeddedCassandraEnvironment {


    @Inject private AccountService accountService;

    @Inject private UserRepository userRepository;

    @Inject private UserRegistrationRepository userRegistrationRepository;

    private  final Logger log = LoggerFactory.getLogger(this.getClass());


    @Test
    public void createUserTest() throws Exception {
        CreateAccountRequest accountRequest = prepareAccount("login");
        String regKey = UserUtil.generateRegKey();
        Mono<User> createUserPublisher = accountService.createUser(accountRequest, regKey);
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
    public void createUser() throws Exception {
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
    public void activateAccount() throws Exception {

        CreateAccountRequest account = prepareAccount("login");
        String regKey = UserUtil.generateRegKey();
        User user = userByAccount(account);
        UserRegistration userRegistration = userRegistrationByAccountAndKey(account, regKey);
        RegisterAccountRequest request = registerAccountRequestByUserAndKey(user, userRegistration);

        Mono<ActivateAccountRequiredInfo> registerAccountMono = accountService.activateAccount(request);

        // No user or registration in db yet
        StepVerifier.create(registerAccountMono).expectError(EntityMissingException.class).verify();

        userRepository.save(user).block();
        // User created but no  registration in db yet
        StepVerifier.create(registerAccountMono).expectError(UserRegistrationNoFound.class).verify();

        userRepository.deleteAll().block();
        userRegistrationRepository.save(userRegistration).block();
        // Registration created but no  user in db yet
        StepVerifier.create(registerAccountMono).expectError(UserNotFound.class).verify();

        createUserOperation(account, regKey, user1->{}, error->{}).block();

        // We have enough information in db to register account
        StepVerifier.create(registerAccountMono)
                .assertNext(info -> {
                    Assert.assertNotNull(info.getUser());
                    Assert.assertNotNull(info.getUser().getPassword());
                    Assert.assertTrue(info.getUser().isActive());
                }).verifyComplete();
    }



    private Mono<User> createUserOperation(CreateAccountRequest accountRequest,   String regKey , Consumer<User> onSuccess,Consumer<? super Throwable> onError){
      return accountService.createUser(accountRequest, regKey) .doOnError(onError::accept).doOnSuccess(onSuccess);
    }

    private CreateAccountRequest prepareAccount(String login) {
        CreateAccountRequest accountRequest = new CreateAccountRequest();
        accountRequest.setLogin(login);
        accountRequest.setFirstName("first name");
        accountRequest.setLastName("last name");
        accountRequest.setEmail("email");
        return accountRequest;
    }

    private User prepareUser(String login){
       return new User(login, "firstName", "lastName", "email@email.com" );
    }


    private User userByAccount(CreateAccountRequest account){
        return  prepareUser(account.getLogin());
    }

    private UserRegistration userRegistrationByAccountAndKey(CreateAccountRequest account, String key){
        return  new UserRegistration(account.getLogin(), key);
    }
    private UserRegistration userRegistrationByUser(User user){
        return  new UserRegistration(user.getLogin(), "123");
    }

    private RegisterAccountRequest registerAccountRequestByUserAndKey(User user, UserRegistration registration){
        String regKey = registration.getPrimaryKey().getRegistrationKey();
        return new RegisterAccountRequest( user.getLogin(), "P@ssw0rd", regKey);
    }

}