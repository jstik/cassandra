package com.jstik.fancy.user.dao.repository;

import com.jstik.fancy.test.util.cassandra.EmbeddedCassandraConfig;
import com.jstik.fancy.test.util.cassandra.EmbeddedCassandraEnvironment;
import com.jstik.fancy.user.dao.UserServiceCassandraConfig;
import com.jstik.fancy.user.entity.UserOperationTimestamp;
import com.jstik.fancy.user.entity.UserOperationTimestamp.UserOperationTimestampPrimaryKey;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import reactor.core.publisher.Flux;

import javax.inject.Inject;

import java.time.LocalDateTime;

import static com.jstik.site.cassandra.model.EntityOperation.CREATE;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {EmbeddedCassandraConfig.class, UserServiceCassandraConfig.class})
@TestPropertySource("classpath:embedded-test.properties")
public class UserOperationTimestampRepositoryTest extends EmbeddedCassandraEnvironment {

    @Inject UserOperationTimestampRepository repository;

    @Test
    public void findAllByPrimaryKeyOperationAndPrimaryKeyTimestampAfter() throws Exception {
        UserOperationTimestamp timestamp = new UserOperationTimestamp(new UserOperationTimestampPrimaryKey(CREATE, LocalDateTime.now(), "login"));
        repository.save(timestamp).block();
        Flux<UserOperationTimestamp> timestampAfter = repository.findAllByPrimaryKeyOperationAndPrimaryKeyTimestampAfter(CREATE, LocalDateTime.now().minusDays(1));
        UserOperationTimestamp userOperationTimestamp = timestampAfter.blockFirst();
        Assert.assertNotNull(userOperationTimestamp);

    }

}