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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import reactor.test.StepVerifier;

import javax.inject.Inject;

import java.time.Duration;

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

@TestPropertySource({"classpath:test.properties","classpath:consul.properties"})

public class UserServiceTest {



    @Inject
    private UserService userService;
    @Inject
    private UserRegistrationRepository userRegistrationRepository;
    @Inject
    private UserRepository userRepository;
    @Inject
    private EntityByTagRepository entityByTagRepository;
    @Inject
    private TagRepository tagRepository;

    @Inject
    private UsersByClientRepository usersByClientRepository;

    @Inject UserOperationsRepository userOperationsRepository;


    @Rule
    @Inject
    public CassandraCreateDropSchemaRule createDropSchemaRule;

    private  final Logger log = LoggerFactory.getLogger(this.getClass());


    @Test
    public void findUserOrThrow() throws Exception {
        User user = TestUserUtil.prepareUser("login");
        //No user in db yet
        StepVerifier.create(userService.findUserOrThrow(user.getLogin())).expectError(UserNotFound.class).verify();

        //Save user to db
        userService.updateUser(user).block();

        StepVerifier.create(userService.findUserOrThrow(user.getLogin())).assertNext(Assert::assertNotNull).verifyComplete();
    }

    @Test
    public void createUser() throws Exception {
        //test create user with no tags, clients,
        User user = TestUserUtil.prepareUser("login");
        String regKey = UserUtil.generateRegKey();
        log.debug("test create user with no tags, clients  userService.createUser ");
        StepVerifier.create(userService.createUser(user, regKey)).assertNext(Assert::assertNotNull).verifyComplete();
        log.debug("test create user with no tags, clients userRepository.findByPrimaryKeyLogin ");
        StepVerifier.create(userRepository.findByPrimaryKeyLogin(user.getLogin())).assertNext(Assert::assertNotNull).verifyComplete();
        log.debug("test create user with no tags, clients userRegistrationRepository.findById ");
        StepVerifier.create(userRegistrationRepository.findById(userRegistration(user, regKey).getPrimaryKey())).assertNext(Assert::assertNotNull).verifyComplete();
        log.debug("test create user with no tags, clients userService.createUser ");
        StepVerifier.create(userService.createUser(user, regKey)).expectError(EntityAlreadyExistsException.class).verify();

        //test create user with  tags

        String userWithTagsLogin = "userWithTags";
        User userWithTags = TestUserUtil.prepareUser(userWithTagsLogin);
        userWithTags.setTags(Sets.newHashSet("tag1", "tag2"));
        StepVerifier.create(userService.createUser(userWithTags, regKey)).assertNext(Assert::assertNotNull).verifyComplete();

        Thread.sleep(200);

        StepVerifier.create(entityByTagRepository.findAllByPrimaryKeyTag("tag1")).assertNext(userByTag -> {
            Assert.assertThat(userByTag.getPrimaryKey().getEntityId(), is(userWithTagsLogin));
        }).verifyComplete();

        StepVerifier.create(tagRepository.findAll()).assertNext(tag -> {
            Assert.assertThat(tag.getName(), anyOf(is("tag2"),  is("tag1")));
        }).assertNext(tag -> {
            Assert.assertThat(tag.getName(), anyOf(is("tag2"),  is("tag1")));
        }).verifyComplete();



        //test create user with  clients
        String userWithClientsLogin = "userWithClients";
        User userWithClients = TestUserUtil.prepareUser(userWithClientsLogin);
        userWithClients.setClients(Sets.newHashSet("client1", "client2"));
        log.debug("test create user with  clients  tags userService.createUser");
        StepVerifier.create(userService.createUser(userWithClients, regKey)).assertNext(Assert::assertNotNull).verifyComplete();
        log.debug("test create user with  clients  tags userRepository.findByPrimaryKeyLogin");
        StepVerifier.create(userRepository.findByPrimaryKeyLogin(userWithClients.getLogin())).assertNext(Assert::assertNotNull).verifyComplete();
        log.debug("test create user with  clients  tags usersByClientRepository.findAll()");
        StepVerifier.create(usersByClientRepository.findAll()).assertNext(usersByClient -> {
            Assert.assertThat(usersByClient.getPrimaryKey().getLogin(), is(userWithClientsLogin));
            Assert.assertThat(usersByClient.getPrimaryKey().getClient(), anyOf(is("client1"),  is("client2")));
        }).assertNext(usersByClient ->{
            Assert.assertThat(usersByClient.getPrimaryKey().getLogin(), is(userWithClientsLogin));
            Assert.assertThat(usersByClient.getPrimaryKey().getClient(), anyOf(is("client1"),  is("client2")));
        }).verifyComplete();
    }




    @Test
    public void updateUser() throws Exception {
        User user = TestUserUtil.prepareUser("login");
        StepVerifier.create(userService.updateUser(user)).assertNext(Assert::assertNotNull)
                .verifyComplete();
        Thread.sleep(1000);
        StepVerifier.create(userOperationsRepository.findAll())
                .assertNext(userOperations -> {
            Assert.assertNotNull(userOperations);
        }).verifyComplete();
    }

    @Test
    public void insertBrandNewUserLinkedInBatch() throws Exception {
        User user = TestUserUtil.prepareUser("login");
        StepVerifier.create(userService.insertBrandNewUserLinkedInBatch(user)).assertNext(Assert::assertTrue).verifyComplete();
    }

    @Test
    public void activateUser() throws Exception {
        StepVerifier.create(userService.activateUser(TestUserUtil.prepareUser("login"), "P@ssword")).assertNext(user -> {
            Assert.assertNotNull(user.getPassword());
        }).verifyComplete();
    }



    private UserRegistration userRegistration(User user, String key) {
        return new UserRegistration(user.getLogin(), key);
    }


}