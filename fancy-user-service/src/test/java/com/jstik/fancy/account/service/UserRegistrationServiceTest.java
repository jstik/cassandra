package com.jstik.fancy.account.service;

import com.jstik.fancy.account.TestApp;
import com.jstik.fancy.account.dao.repository.cassandra.UserServiceCassandraConfig;
import com.jstik.fancy.account.security.UserServiceSecurityConfig;
import com.jstik.fancy.account.util.UserUtil;
import com.jstik.fancy.test.util.cassandra.CassandraCreateDropSchemaRule;
import com.jstik.fancy.test.util.cassandra.EmbeddedCassandraConfig;
import com.jstik.site.discovery.stub.StubLoadBalancerConfig;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringJUnitWebConfig
@ContextConfiguration(
        classes = {
                TestApp.class,
                EmbeddedCassandraConfig.class,
                StubLoadBalancerConfig.class
        }
)

@TestPropertySource({"classpath:embedded-test.properties", "classpath:consul.properties"})
public class UserRegistrationServiceTest {

    @Rule
    @Inject
    public CassandraCreateDropSchemaRule createDropSchemaRule;

    @Inject
    UserRegistrationService userRegistrationService;


    @Test
    public void createRegistration() throws Exception {
    }

    @Test
    public void findRegistration() throws Exception {
    }

    @Test
    public void delete() throws Exception {
    }

    @Test
    public void regKeyToRegistrationUrl() throws Exception {

        String regKey = UserUtil.generateRegKey();

        userRegistrationService.regKeyToRegistrationUrl("userLogin", regKey);
    }

}