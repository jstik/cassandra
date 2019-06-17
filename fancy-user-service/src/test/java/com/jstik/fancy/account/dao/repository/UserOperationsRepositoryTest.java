package com.jstik.fancy.account.dao.repository;

import com.google.common.collect.Sets;
import com.jstik.fancy.account.dao.UserServiceCassandraConfig;
import com.jstik.fancy.account.entity.user.User;
import com.jstik.fancy.account.entity.user.UserOperations;
import com.jstik.fancy.test.util.cassandra.CassandraCreateDropSchemaRule;
import com.jstik.fancy.test.util.cassandra.EmbeddedCassandraConfig;
import com.jstik.site.cassandra.model.EntityOperation;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import reactor.test.StepVerifier;

import javax.inject.Inject;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {EmbeddedCassandraConfig.class, UserServiceCassandraConfig.class})
@TestPropertySource("classpath:embedded-test.properties")
public class UserOperationsRepositoryTest {

    @Rule
    @Inject
    public CassandraCreateDropSchemaRule createDropSchemaRule;

    @Inject
    public UserOperationsRepository repository;


    @Test
    public void save(){
        UserOperations record = new UserOperations(prepareUser("login"), EntityOperation.CREATE);

        StepVerifier.create(repository.save(record)).expectNext(record).verifyComplete();

        record.getPrimaryKey().setTimestamp(LocalDateTime.now(Clock.systemUTC()));

        StepVerifier.create(repository.save(record)).expectNext(record).verifyComplete();

        StepVerifier.create(repository.count()).expectNext(2L).verifyComplete();

    }

    @Test
    public void findAllByPrimaryKeyDay(){
        StepVerifier.create(repository.findAllByPrimaryKeyDay(LocalDate.now(Clock.systemUTC()))).verifyComplete();
    }

    private User prepareUser(String login) {
        User user = new User(login, "firstName", "lastName", "user@user.com");
        user.setClients(Sets.newHashSet("client1", "client2"));
        return user;
    }


}