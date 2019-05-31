package com.jstik.fancy.user.service;

import com.jstik.fancy.test.util.cassandra.EmbeddedCassandraConfig;
import com.jstik.fancy.test.util.cassandra.EmbeddedCassandraEnvironment;
import com.jstik.fancy.user.dao.UserServiceCassandraConfig;
import com.jstik.fancy.user.dao.repository.UserRegistrationRepository;
import com.jstik.fancy.user.dao.repository.UserRepository;
import com.jstik.fancy.user.entity.User;
import com.jstik.fancy.user.entity.UserRegistration;
import com.jstik.fancy.user.entity.UserRegistration.UserRegistrationPrimaryKey;
import com.jstik.fancy.user.model.account.RegisterAccountRequest;
import com.jstik.fancy.user.web.UserServiceWebConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import javax.inject.Inject;
import java.util.concurrent.CountDownLatch;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringJUnitWebConfig
@ContextConfiguration(
        classes = {
                EmbeddedCassandraConfig.class, UserServiceCassandraConfig.class,
                ServiceConfig.class, UserServiceWebConfig.class
        }
)
@TestPropertySource("classpath:embedded-test.properties")
public class UserCompositeTest extends EmbeddedCassandraEnvironment {

    @Inject
    private UserRepository userRepository;

    @Inject
    private UserRegistrationRepository userRegistrationRepository;

    private  final Logger log = LoggerFactory.getLogger(this.getClass());


    @Test
    public void registerAccountTest() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);

        RegisterAccountRequest registerAccount = new RegisterAccountRequest("login", "passord", "123");

        userRegistrationRepository.insert(new UserRegistration("login","123" )).block();

        Mono<UserRegistration> findUserRegistrationOperation = userRegistrationRepository.findById(registrationPK(registerAccount))
                .log().doOnSuccess(reg-> {
                    log.debug(" reg found");
                    if(reg == null)
                        throw new RuntimeException("No registration");

                });

        Mono<User> findUserOperation = userRepository.findById(registerAccount.getLogin()).log().doOnSuccess(reg-> {
            if(reg == null)
                throw new RuntimeException("No registration");
            log.debug(" user found");
        });

        Mono<Tuple2<UserRegistration, User>> userRegistrationMono = Mono.zip(findUserRegistrationOperation, findUserOperation);

        userRegistrationMono.subscribe(reg->{
            log.debug(" composite found") ;
        }, error->{
            log.debug("error") ;
            countDownLatch.countDown();
        }, () ->{
            log.debug("complete") ;
            countDownLatch.countDown();
        });

        countDownLatch.await();

    }

    private UserRegistrationPrimaryKey registrationPK( RegisterAccountRequest registerAccount){
      return  new UserRegistrationPrimaryKey(registerAccount.getLogin(), registerAccount.getRegKey());
    }
}
