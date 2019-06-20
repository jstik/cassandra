package com.jstik.fancy.account.dao.repository;

import com.jstik.fancy.account.dao.UserServiceCassandraConfig;
import com.jstik.fancy.test.util.cassandra.CassandraCreateDropSchemaRule;
import com.jstik.fancy.test.util.cassandra.EmbeddedCassandraConfig;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import reactor.test.StepVerifier;

import javax.inject.Inject;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {EmbeddedCassandraConfig.class, UserServiceCassandraConfig.class})
@TestPropertySource("classpath:embedded-test.properties")
public class TagRepositoryTest {

    @Rule
    @Inject
    public CassandraCreateDropSchemaRule createDropSchemaRule;

    @Inject
    TagRepository repository;

    @Test
    public void saveTag() throws Exception {

        repository.saveTag("tag1");

        StepVerifier.create(repository.findById("tag1")).assertNext(tag -> {
            Assert.assertNotNull(tag);
            Assert.assertThat(tag.getCounter(), is(1));
        });

        repository.saveTag("tag1");

        StepVerifier.create(repository.findById("tag1")).assertNext(tag -> {
            Assert.assertNotNull(tag);
            Assert.assertThat(tag.getCounter(), is(2));
        });
    }

}