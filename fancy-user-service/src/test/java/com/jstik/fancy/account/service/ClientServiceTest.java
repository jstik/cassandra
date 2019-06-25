package com.jstik.fancy.account.service;

import com.google.common.collect.Sets;
import com.jstik.fancy.account.dao.UserServiceCassandraConfig;
import com.jstik.fancy.account.dao.repository.UsersByClientRepository;
import com.jstik.fancy.account.entity.cassandra.user.User;
import com.jstik.fancy.test.util.cassandra.CassandraCreateDropSchemaRule;
import com.jstik.fancy.test.util.cassandra.EmbeddedCassandraConfig;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import reactor.test.StepVerifier;

import javax.inject.Inject;

import static org.hamcrest.core.AnyOf.anyOf;
import static org.hamcrest.core.Is.is;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringJUnitWebConfig
@ContextConfiguration(
        classes = {
                EmbeddedCassandraConfig.class, UserServiceCassandraConfig.class,
                LinkedServicesConfig.class,
        }
)
@TestPropertySource({"classpath:embedded-test.properties"})
public class ClientServiceTest {

    @Rule
    @Inject
    public CassandraCreateDropSchemaRule createDropSchemaRule;

    @Inject
    private ClientService clientService;

    @Inject
    private UsersByClientRepository usersByClientRepository;

    @Test
    public void addUserClients() throws Exception {
        User user = TestUserUtil.prepareUser("login");

        StepVerifier.create(clientService.addUserClients(user, Sets.newHashSet("client1", "client2")))
                .assertNext(Assert::assertTrue).verifyComplete();
        StepVerifier.create(usersByClientRepository.findAll()).assertNext(userClient->{
            Assert.assertThat(userClient.getPrimaryKey().getClient(), anyOf(is("client1"),  is("client2")));
        }).assertNext(userClient->{
            Assert.assertThat(userClient.getPrimaryKey().getClient(), anyOf(is("client1"),  is("client2")));
        }).verifyComplete();
    }

    @Test
    public void deleteUserClients() throws Exception {
        User user = TestUserUtil.prepareUser("login");
        clientService.addUserClients(user, Sets.newHashSet("client1", "client2")).block();
        StepVerifier.create(clientService.deleteUserClients(user, Sets.newHashSet("client1"))).verifyComplete();
        StepVerifier.create(usersByClientRepository.findAll()).assertNext(userClient->{
            Assert.assertThat(userClient.getPrimaryKey().getClient(),is("client2"));
        }).verifyComplete();

    }

    @Test
    public void insertUsersByClientStatement() throws Exception {
    }

}