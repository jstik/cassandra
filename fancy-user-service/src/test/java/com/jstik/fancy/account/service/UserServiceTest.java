package com.jstik.fancy.account.service;

import com.google.common.collect.Sets;
import com.jstik.fancy.account.TestApp;
import com.jstik.fancy.account.dao.repository.*;
import com.jstik.fancy.test.util.cassandra.CassandraCreateDropSchemaRule;
import com.jstik.fancy.test.util.cassandra.EmbeddedCassandraConfig;
import com.jstik.fancy.account.dao.UserServiceCassandraConfig;
import com.jstik.fancy.account.entity.user.User;
import com.jstik.fancy.account.entity.user.UserRegistration;
import com.jstik.fancy.account.exception.UserNotFound;
import com.jstik.fancy.account.security.UserServiceSecurityConfig;
import com.jstik.fancy.account.util.UserUtil;
import com.jstik.fancy.account.web.UserServiceWebConfig;
import com.jstik.site.cassandra.exception.EntityAlreadyExistsException;
import com.jstik.site.discovery.stub.StubLoadBalancerConfig;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import javax.inject.Inject;

import static org.hamcrest.core.AnyOf.anyOf;
import static org.hamcrest.core.Is.is;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringJUnitWebConfig
@ContextConfiguration(
        classes = {
                TestApp.class,
                EmbeddedCassandraConfig.class, UserServiceCassandraConfig.class,
                ServiceConfig.class, UserServiceWebConfig.class,
                UserServiceSecurityConfig.class,
                StubLoadBalancerConfig.class
        }
)

@TestPropertySource({"classpath:embedded-test.properties","classpath:consul.properties"})

public class UserServiceTest {


    @Inject
    private UserService userService;
    @Inject
    private UserRegistrationRepository userRegistrationRepository;
    @Inject
    private UserRepository userRepository;
    @Inject
    private UserByTagRepository userByTagRepository;
    @Inject
    private TagRepository tagRepository;

    @Inject
    private UsersByClientRepository usersByClientRepository;


    @Rule
    @Inject
    public CassandraCreateDropSchemaRule createDropSchemaRule;


    @Test
    public void getUserToActivate() throws Exception {
        User user = prepareUser("login");
        String password = "P@ssword";
        //No user in db yet
        StepVerifier.create(userService.getUserToActivate(user.getLogin(), password)).expectError(UserNotFound.class).verify();

        //Save user to db
        userService.saveUserAndDelayUntil(user, Mono.empty()).block();

        StepVerifier.create(userService.getUserToActivate(user.getLogin(), password)).assertNext(created -> {
            Assert.assertTrue(created.isActive());
            Assert.assertNotNull(created.getPassword());
        }).verifyComplete();
    }

    @Test
    public void createUser() throws Exception {
        //test create user with no tags, clients,
        User user = prepareUser("login");
        String regKey = UserUtil.generateRegKey();
        StepVerifier.create(userService.createUser(user, regKey)).assertNext(Assert::assertNotNull).verifyComplete();
        StepVerifier.create(userRepository.findByPrimaryKeyLogin(user.getLogin())).assertNext(Assert::assertNotNull).verifyComplete();
        StepVerifier.create(userRegistrationRepository.findById(userRegistration(user, regKey).getPrimaryKey())).assertNext(Assert::assertNotNull).verifyComplete();
        StepVerifier.create(userService.createUser(user, regKey)).expectError(EntityAlreadyExistsException.class).verify();

        //test create user with  tags

        User userWithTags = prepareUser("login1");
        userWithTags.setTags(Sets.newHashSet("tag1", "tag2"));
        StepVerifier.create(userService.createUser(userWithTags, regKey)).assertNext(Assert::assertNotNull).verifyComplete();
        StepVerifier.create(userRepository.findByPrimaryKeyLogin(userWithTags.getLogin())).assertNext(Assert::assertNotNull).verifyComplete();
        StepVerifier.create(tagRepository.findAll()).assertNext(tag -> {
            Assert.assertThat(tag.getName(), anyOf(is("tag2"),  is("tag1")));
        }).assertNext(tag -> {
            Assert.assertThat(tag.getName(), anyOf(is("tag2"),  is("tag1")));
        }).verifyComplete();
        StepVerifier.create(userByTagRepository.findAllByPrimaryKeyTag("tag1")).assertNext(userByTag -> {
            Assert.assertThat(userByTag.getPrimaryKey().getLogin(), is("login1"));
        }).verifyComplete();

        //test create user with  clients
        User userWithClients = prepareUser("userWithClients");
        userWithClients.setClients(Sets.newHashSet("client1", "client2"));
        StepVerifier.create(userService.createUser(userWithClients, regKey)).assertNext(Assert::assertNotNull).verifyComplete();
        StepVerifier.create(userRepository.findByPrimaryKeyLogin(userWithClients.getLogin())).assertNext(Assert::assertNotNull).verifyComplete();
        StepVerifier.create(usersByClientRepository.findAll()).assertNext(usersByClient -> {
            Assert.assertThat(usersByClient.getPrimaryKey().getLogin(), is("userWithClients"));
            Assert.assertThat(usersByClient.getPrimaryKey().getClient(), anyOf(is("client1"),  is("client2")));
        }).assertNext(usersByClient ->{
            Assert.assertThat(usersByClient.getPrimaryKey().getLogin(), is("userWithClients"));
            Assert.assertThat(usersByClient.getPrimaryKey().getClient(), anyOf(is("client1"),  is("client2")));
        }).verifyComplete();
    }

    @Test
    public void regKeyToRegistrationUrl() throws Exception {
        String regKey = UserUtil.generateRegKey();

        userService.regKeyToRegistrationUrl("userLogin", regKey);
    }


    @Test
    public void saveUserAndDelayUntil() throws Exception {
        User user = prepareUser("login");
        StepVerifier.create(userService.saveUserAndDelayUntil(user, Mono.empty())).assertNext(Assert::assertNotNull).verifyComplete();
    }

    @Test
    public void insertBrandNewUserLinkedInBatch() throws Exception {
        User user = prepareUser("login");
        StepVerifier.create(userService.insertBrandNewUserLinkedInBatch(user)).assertNext(Assert::assertTrue).verifyComplete();
    }

    private User prepareUser(String login) {
        return new User(login, "firstName", "lastName", "email@email.com");
    }

    private UserRegistration userRegistration(User user, String key) {
        return new UserRegistration(user.getLogin(), key);
    }


}