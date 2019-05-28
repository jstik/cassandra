package com.jstik.fancy.chat.dao.repository;


import com.jstik.fancy.chat.dao.config.CassandraConfig;
import com.jstik.fancy.chat.model.entity.User;
import com.jstik.fancy.test.util.cassandra.EmbeddedCassandraConfig;
import com.jstik.fancy.test.util.cassandra.EmbeddedCassandraEnvironment;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import reactor.core.publisher.Mono;

import javax.inject.Inject;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {EmbeddedCassandraConfig.class, CassandraConfig.class})
@TestPropertySource("classpath:embedded-test.properties")
public class UserRepositoryTest extends EmbeddedCassandraEnvironment {

    @Inject
    private UserRepository userRepository;

    @Test
    public void  testCreateUser(){
        Mono<User> userByLoginOperation = userRepository.findById("login");
        Mono<User> saveUserOperation = userRepository.save(prepareUser());
        saveUserOperation.block();
        User userFromDb = userByLoginOperation.block();
        Assert.assertNotNull(userFromDb);
    }

    @Test
    public void testFinById() throws InterruptedException {
        Mono<User> saveUserOperation = userRepository.save(prepareUser());
        saveUserOperation.block();
        final User[] userFromDb = {null};
        userRepository.findById("login").subscribe(user -> {
            userFromDb[0] = user;
        });

        Thread.sleep(200);
        Assert.assertNotNull(userFromDb[0]);
    }

    private User prepareUser(){
        User testUser = new User();
        testUser.setLogin("login");
        testUser.setName("user Name");
        return  testUser;
    }

}