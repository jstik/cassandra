package com.jstik.fancy.account.storage.service;

import com.jstik.fancy.account.model.exception.UserRegistrationNoFound;
import com.jstik.fancy.account.security.UserServiceSecurityConfig;
import com.jstik.fancy.account.storage.StorageTestApp;
import com.jstik.fancy.account.util.UserUtil;
import com.jstik.fancy.test.util.cassandra.CassandraCreateDropSchemaRule;
import com.jstik.fancy.test.util.cassandra.EmbeddedCassandraConfig;
import com.jstik.site.discovery.stub.StubLoadBalancerConfig;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import reactor.test.StepVerifier;

import javax.inject.Inject;

import static org.hamcrest.core.Is.is;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringJUnitWebConfig
@ContextConfiguration(
        classes = {
                StorageTestApp.class,
                EmbeddedCassandraConfig.class,
                StubLoadBalancerConfig.class,
                UserServiceSecurityConfig.class
        }
)

@TestPropertySource({"classpath:embedded-test.properties", "classpath:consul.properties"})
public class UserRegistrationServiceTest {


    @Rule
    @Inject
    public CassandraCreateDropSchemaRule createDropSchemaRule;

    @Inject
    private UserRegistrationService userRegistrationService;

    @Inject
    private PasswordEncoder passwordEncoder;


    @Test
    public void createRegistration() throws Exception {
        String regKey = UserUtil.generateRegKey();
        StepVerifier.create(userRegistrationService.createRegistration("userLogin", regKey))
                .assertNext(Assert::assertNotNull).verifyComplete();
    }


    @Test
    public void delete() throws Exception {
        String regKey = UserUtil.generateRegKey();
        String userLogin = "userLogin";
        //create  registration in db
        userRegistrationService.createRegistration(userLogin, regKey).block();
        //make sure it exists
        StepVerifier.create(userRegistrationService.findRegistrationOrThrow(userLogin, regKey))
                .assertNext(Assert::assertNotNull).verifyComplete();

        //delete registration
        StepVerifier.create(userRegistrationService.delete(userLogin, regKey)).verifyComplete();

        //verify that registration is deleted
        StepVerifier.create(userRegistrationService.findRegistrationOrThrow(userLogin, regKey))
                .expectError(UserRegistrationNoFound.class).verify();
    }

    @Test
    public void encodePassword() throws Exception {
        String plain = "P@ssw0rd";
        String result = userRegistrationService.encodePassword(plain);
        Assert.assertFalse(result.equals(plain));
        Assert.assertTrue(passwordEncoder.matches(plain, result));
    }

    @Test
    public void findRegistrationOrThrow() throws Exception {
        String regKey = UserUtil.generateRegKey();
        String userLogin = "userLogin";
        //no certain registration in db
        StepVerifier.create(userRegistrationService.findRegistrationOrThrow(userLogin, regKey))
                .expectError(UserRegistrationNoFound.class).verify();

        //create  registration in db
        userRegistrationService.createRegistration(userLogin, regKey).block();

        StepVerifier.create(userRegistrationService.findRegistrationOrThrow(userLogin, regKey))
                .assertNext(reg->{
                    Assert.assertNotNull(reg);
                    Assert.assertThat(reg.getLogin(), is(userLogin));
                    Assert.assertThat(reg.getRegKey(), is(regKey));
                }).verifyComplete();

    }

    @Test
    public void regKeyToRegistrationUrl() throws Exception {

        String regKey = UserUtil.generateRegKey();

        userRegistrationService.regKeyToRegistrationUrl("userLogin", regKey);
    }

}