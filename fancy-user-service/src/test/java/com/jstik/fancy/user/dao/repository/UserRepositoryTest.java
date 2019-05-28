package com.jstik.fancy.user.dao.repository;

import com.jstik.fancy.test.util.cassandra.EmbeddedCassandraConfig;
import com.jstik.fancy.test.util.cassandra.EmbeddedCassandraEnvironment;
import com.jstik.fancy.user.dao.UserServiceCassandraConfig;
import com.jstik.fancy.user.entity.User;
import com.jstik.site.cassandra.exception.EntityAlreadyExistsException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {EmbeddedCassandraConfig.class, UserServiceCassandraConfig.class})
@TestPropertySource("classpath:embedded-test.properties")
public class UserRepositoryTest extends EmbeddedCassandraEnvironment {

    @Inject
    private UserRepository userRepository;

    @Test
    public void saveUserTest() {
        String userLogin = "login";
        userRepository.save(prepareUser(userLogin)).block();
        User user = userRepository.findById(userLogin).block();
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
        User existingUser = userRepository.save(prepareUser("login")).block();
        userRepository.insertIfNotExistOrThrow(existingUser).block();
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