package com.jstik.fancy.user.service;

import com.jstik.fancy.test.util.cassandra.EmbeddedCassandraConfig;
import com.jstik.fancy.test.util.cassandra.EmbeddedCassandraEnvironment;
import com.jstik.fancy.user.dao.UserServiceCassandraConfig;
import com.jstik.fancy.user.entity.User;
import com.jstik.fancy.user.model.CreateAccountRequest;
import com.jstik.fancy.user.util.UserUtil;
import com.jstik.fancy.user.web.UserServiceWebConfig;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.web.servlet.support.ServletContextApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import reactor.core.publisher.Mono;

import javax.inject.Inject;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringJUnitWebConfig
@ContextConfiguration(
        /*initializers = {ServletContextApplicationContextInitializer.class},*/
        classes = {EmbeddedCassandraConfig.class, UserServiceCassandraConfig.class, ServiceConfig.class, UserServiceWebConfig.class}
        )
@TestPropertySource("classpath:embedded-test.properties")
public class UserServiceTest extends EmbeddedCassandraEnvironment {

    private @Inject UserService userService;


    @Test
    public void createUser() throws Exception {
        CreateAccountRequest accountRequest = new CreateAccountRequest();
        accountRequest.setLogin("login");
        accountRequest.setFirstName("first name");
        accountRequest.setLastName("last name");
        accountRequest.setEmail("email");

        String regKey = UserUtil.generateRegKey();
        Mono<User> user = userService.createUser(accountRequest, regKey);
        System.out.println("--------------- regKey " + "ajsdiajsdjajsdldjsl");
        Assert.assertNotNull(userService);
    }

}