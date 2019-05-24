package com.jstik.fancy.chat.dao.repository;


import com.jstik.fancy.chat.dao.config.CassandraConfig;
import com.jstik.fancy.chat.model.User;
import com.jstik.fancy.util.EmbeddedCassandraEnvironment;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import javax.inject.Inject;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CassandraConfig.class})
@TestPropertySource("classpath:test.properties")
public class UserRepositoryTest extends EmbeddedCassandraEnvironment {

    @Inject UserRepository userRepository;

    User testUser;

    @Before
    public void initTestUser(){
        testUser = new User();
        testUser.setLogin("login");
        testUser.setName("user Name");
    }

    @Test
    public void  testCreateUser(){
        Mono<User> userByLoginOperation = userRepository.findById("login");
        Mono<User> saveUserOperation = userRepository.save(testUser);
        saveUserOperation.block();
        User userFromDb = userByLoginOperation.block();
        Assert.assertNotNull(userFromDb);
    }

    public void testFinById(){

    }

}