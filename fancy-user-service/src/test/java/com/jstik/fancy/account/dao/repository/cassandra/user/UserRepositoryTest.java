package com.jstik.fancy.account.dao.repository.cassandra.user;

import com.jstik.fancy.account.dao.repository.cassandra.UserServiceCassandraConfig;
import com.jstik.fancy.test.util.cassandra.CassandraCreateDropSchemaRule;
import com.jstik.fancy.test.util.cassandra.EmbeddedCassandraConfig;
import com.jstik.fancy.account.entity.cassandra.user.User;
import com.jstik.site.cassandra.exception.EntityAlreadyExistsException;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {EmbeddedCassandraConfig.class, UserServiceCassandraConfig.class})
@TestPropertySource("classpath:embedded-test.properties")
public class UserRepositoryTest {

    @Inject
    private UserRepository userRepository;
    @Rule
    @Inject
    public CassandraCreateDropSchemaRule createDropSchemaRule;

    @Test
    public void saveUserTest() {
        String userLogin = "login";
        userRepository.save(prepareUser(userLogin)).block();
        User user = userRepository.findByPrimaryKeyLogin(userLogin).block();
        Assert.assertNotNull(user);

    }

    @Test
    public void insertUserIfNotExistsTest() {
        User existingUser = userRepository.save(prepareUser("login")).block();
        Assert.assertFalse(userRepository.insertIfNotExist(existingUser).block());
        Assert.assertTrue(userRepository.insertIfNotExist(prepareUser("NewLogin")).block());
    }


    @Test(expected = EntityAlreadyExistsException.class)
    public void insertIfNotExistOrThrowTest() {
        userRepository.save(prepareUser("login")).block();
        userRepository.insertIfNotExistOrThrow(prepareUser("login")).block();
    }


    @Test
    public void updateIfExistTest(){
        User existingUser = userRepository.save(prepareUser("login")).block();
        Assert.assertTrue(userRepository.updateIfExist(existingUser).block());
    }


    private User prepareUser(String login) {
        return new User(login, "firstName", "lastName", "user@user.com");
    }

}