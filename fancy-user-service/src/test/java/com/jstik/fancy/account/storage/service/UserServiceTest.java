package com.jstik.fancy.account.storage.service;

import com.google.common.collect.Sets;
import com.jstik.fancy.account.model.exception.UserNotFound;
import com.jstik.fancy.account.storage.dao.repository.cassandra.UserServiceCassandraConfig;
import com.jstik.fancy.account.storage.dao.repository.cassandra.client.UsersByClientRepository;
import com.jstik.fancy.account.storage.dao.repository.cassandra.tag.EntityByTagRepository;
import com.jstik.fancy.account.storage.dao.repository.cassandra.tag.TagRepository;
import com.jstik.fancy.account.storage.dao.repository.cassandra.user.UserOperationsRepository;
import com.jstik.fancy.account.storage.dao.repository.cassandra.user.UserRegistrationRepository;
import com.jstik.fancy.account.storage.dao.repository.cassandra.user.UserRepository;
import com.jstik.fancy.account.storage.entity.cassandra.user.User;
import com.jstik.fancy.account.storage.entity.cassandra.user.UserRegistration;
import com.jstik.fancy.account.util.UserUtil;
import com.jstik.fancy.test.util.cassandra.CassandraCreateDropSchemaRule;
import com.jstik.fancy.test.util.cassandra.EmbeddedCassandraConfig;
import com.jstik.site.cassandra.exception.EntityAlreadyExistsException;
import org.hamcrest.collection.IsCollectionWithSize;
import org.hamcrest.collection.IsIterableContainingInOrder;
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
import reactor.core.publisher.Mono;

import javax.inject.Inject;

import static com.google.common.collect.Sets.newHashSet;
import static com.jstik.fancy.account.storage.service.TestUserUtil.prepareUser;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.hamcrest.core.AnyOf.anyOf;
import static org.hamcrest.core.Is.is;
import static reactor.test.StepVerifier.create;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringJUnitWebConfig
@ContextConfiguration(
        classes = {
                EmbeddedCassandraConfig.class, UserServiceCassandraConfig.class,
                LinkedServicesConfig.class
        }
)

@TestPropertySource({"classpath:test.properties", "classpath:consul.properties"})
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

    @Inject
    UserOperationsRepository userOperationsRepository;


    @Rule
    @Inject
    public CassandraCreateDropSchemaRule createDropSchemaRule;

    private final Logger log = LoggerFactory.getLogger(this.getClass());


    @Test
    public void findUserOrThrow() throws Exception {
        User user = prepareUser("login");
        //No user in db yet
        create(userService.findUserOrThrow(user.getLogin())).expectError(UserNotFound.class).verify();

        //Save user to db
        userService.updateUser(user).block();

        create(userService.findUserOrThrow(user.getLogin())).assertNext(Assert::assertNotNull).verifyComplete();
    }

    @Test
    public void createUser() throws Exception {
        //test create user with no tags, clients,
        User user = prepareUser("login");
        String regKey = UserUtil.generateRegKey();
        log.debug("test create user with no tags, clients  userService.createUser ");
        Mono<UserRegistration> registration = userRegistrationRepository.save(new UserRegistration(user.getLogin(), regKey));
        create(userService.createUser(user, registration)).assertNext(Assert::assertNotNull).verifyComplete();
        log.debug("test create user with no tags, clients userRepository.findByPrimaryKeyLogin ");
        create(userRepository.findByPrimaryKeyLogin(user.getLogin())).assertNext(Assert::assertNotNull).verifyComplete();
        log.debug("test create user with no tags, clients userRegistrationRepository.findById ");
        create(userRegistrationRepository.findById(userRegistration(user, regKey).getPrimaryKey())).assertNext(Assert::assertNotNull).verifyComplete();
        log.debug("test create user with no tags, clients userService.createUser ");
        create(userService.createUser(user, registration)).expectError(EntityAlreadyExistsException.class).verify();

        //test create user with  tags

        String userWithTagsLogin = "userWithTags";
        User userWithTags = prepareUser(userWithTagsLogin);
        userWithTags.setTags(newHashSet("tag1", "tag2"));
        create(userService.createUser(userWithTags, registration)).assertNext(Assert::assertNotNull).verifyComplete();

        Thread.sleep(200);

        create(entityByTagRepository.findAllByPrimaryKeyTag("tag1")).assertNext(userByTag -> {
            Assert.assertThat(userByTag.getPrimaryKey().getEntityId(), is(userWithTagsLogin));
        }).verifyComplete();

        create(tagRepository.findAll()).assertNext(tag -> {
            Assert.assertThat(tag.getName(), anyOf(is("tag2"), is("tag1")));
        }).assertNext(tag -> {
            Assert.assertThat(tag.getName(), anyOf(is("tag2"), is("tag1")));
        }).verifyComplete();


        //test create user with  clients
        String userWithClientsLogin = "userWithClients";
        User userWithClients = prepareUser(userWithClientsLogin);
        userWithClients.setClients(newHashSet("client1", "client2"));
        log.debug("test create user with  clients  tags userService.createUser");
        create(userService.createUser(userWithClients, registration)).assertNext(Assert::assertNotNull).verifyComplete();
        log.debug("test create user with  clients  tags userRepository.findByPrimaryKeyLogin");
        create(userRepository.findByPrimaryKeyLogin(userWithClients.getLogin())).assertNext(Assert::assertNotNull).verifyComplete();
        log.debug("test create user with  clients  tags usersByClientRepository.findAll()");
        create(usersByClientRepository.findAll()).assertNext(usersByClient -> {
            Assert.assertThat(usersByClient.getPrimaryKey().getLogin(), is(userWithClientsLogin));
            Assert.assertThat(usersByClient.getPrimaryKey().getClient(), anyOf(is("client1"), is("client2")));
        }).assertNext(usersByClient -> {
            Assert.assertThat(usersByClient.getPrimaryKey().getLogin(), is(userWithClientsLogin));
            Assert.assertThat(usersByClient.getPrimaryKey().getClient(), anyOf(is("client1"), is("client2")));
        }).verifyComplete();
    }


    @Test
    public void updateUser() throws Exception {
        User user = prepareUser("login");
        create(userService.updateUser(user)).assertNext(Assert::assertNotNull)
                .verifyComplete();
        Thread.sleep(1000);
        create(userOperationsRepository.findAll()).assertNext(Assert::assertNotNull).verifyComplete();
    }

    @Test
    public void insertBrandNewUserLinkedInBatch() throws Exception {
        User user = prepareUser("login");
        create(userService.insertBrandNewUserLinkedInBatch(user)).assertNext(Assert::assertTrue).verifyComplete();
    }

    @Test
    public void activateUser() throws Exception {
        create(userService.activateUser(prepareUser("login"), "P@ssword")).assertNext(user -> {
            Assert.assertNotNull(user.getPassword());
        }).verifyComplete();
    }


    private UserRegistration userRegistration(User user, String key) {
        return new UserRegistration(user.getLogin(), key);
    }


    @Test
    public void addUserTags() {
        User user = prepareUser("login");
        userService.createUser(user, Mono.empty()).block();
        create(userService.addUserTags(newHashSet("tag1"), user))
                .assertNext(Assert::assertNotNull).verifyComplete();

    }

    @Test
    public void deleteUserTags() {
        User user = prepareUser("login");
        user.setTags(newHashSet("tag1", "tag2"));
        userService.createUser(user, Mono.empty()).block();

        create(userService.deleteUserTags(newHashSet("tag1"), user))
                .assertNext(updated-> {
                    Assert.assertThat(updated.getTags(), hasSize(1));
                    Assert.assertThat(updated.getTags(), contains("tag2"));
                }).verifyComplete();

        create(userService.findUserOrThrow("login")).assertNext(dbUser-> {
            Assert.assertThat(dbUser.getTags(), hasSize(1));
            Assert.assertThat(dbUser.getTags(), contains("tag2"));
        }).verifyComplete();
    }

    @Test
    public void addUserClients() throws Exception {
        User user = prepareUser("login");
        userService.createUser(user, Mono.empty()).block();

        create(userService.addUserClients(newHashSet("client1"), user))
                .assertNext(Assert::assertNotNull).verifyComplete();

        create(userService.findUserOrThrow("login")).assertNext(dbUser-> {
            Assert.assertThat(dbUser.getClients(), hasSize(1));
            Assert.assertThat(dbUser.getClients(), contains("client1"));
        }).verifyComplete();

    }

    @Test
    public void deleteUserClients() throws Exception {
        User user = prepareUser("login");
        user.setClients(newHashSet("client1", "client2"));
        userService.createUser(user, Mono.empty()).block();

        create(userService.deleteUserClients(newHashSet("client1"), user))
                .assertNext(Assert::assertNotNull).verifyComplete();

        create(userService.findUserOrThrow("login")).assertNext(dbUser-> {
            Assert.assertThat(dbUser.getClients(), hasSize(1));
            Assert.assertThat(dbUser.getClients(), contains("client2"));
        }).verifyComplete();
    }

    @Test
    public void insertUser() {
        create(userService.insertUser(prepareUser("login"))) .assertNext(Assert::assertNotNull).verifyComplete();
    }
}