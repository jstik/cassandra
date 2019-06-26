package com.jstik.fancy.account.dao.repository.cassandra.tag;

import com.jstik.fancy.account.dao.repository.cassandra.UserServiceCassandraConfig;
import com.jstik.fancy.account.entity.cassandra.tag.EntityByTag;
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

import java.util.HashSet;

import static com.google.common.collect.Sets.newHashSet;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {EmbeddedCassandraConfig.class, UserServiceCassandraConfig.class})
@TestPropertySource("classpath:embedded-test.properties")
public class EntityByTagRepositoryTest {

    @Rule
    @Inject
    public CassandraCreateDropSchemaRule createDropSchemaRule;

    @Inject
    private EntityByTagRepository entityByTagRepository;

    @Test
    public void findAllByPrimaryKeyTag() {
        entityByTagRepository.save(prepareEntityByTag("tag1")).block();
        StepVerifier.create(entityByTagRepository.findAllByPrimaryKeyTag("tag1"))
                .assertNext(Assert::assertNotNull).verifyComplete();
    }

    @Test
    public void deleteEntityByTags() {
        entityByTagRepository.save(prepareEntityByTag("tag1")).block();
        entityByTagRepository.save(prepareEntityByTag("tag2")).block();
        HashSet<String> tags = newHashSet("tag1", "tag2");
        StepVerifier.create(
                entityByTagRepository.deleteEntityByTags("login", "Discriminator", tags)
        ).verifyComplete();

        StepVerifier.create(entityByTagRepository.findAll()).verifyComplete();
    }

    private EntityByTag prepareEntityByTag(String tag){
      return new EntityByTag(tag, "login", "Discriminator");
    }
}