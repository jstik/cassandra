package com.jstik.fancy.account.storage.service;

import com.jstik.fancy.account.storage.dao.repository.cassandra.UserServiceCassandraConfig;
import com.jstik.fancy.account.storage.dao.repository.cassandra.tag.EntityByTagRepository;
import com.jstik.fancy.account.storage.dao.repository.cassandra.tag.TagRepository;
import com.jstik.fancy.account.storage.entity.cassandra.user.User;
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

import static com.google.common.collect.Sets.newHashSet;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringJUnitWebConfig
@ContextConfiguration(
        classes = {
                EmbeddedCassandraConfig.class, UserServiceCassandraConfig.class,
                LinkedServicesConfig.class,
        }
)
@TestPropertySource({"classpath:embedded-test.properties"})
public class TagServiceTest {

    @Rule
    @Inject
    public CassandraCreateDropSchemaRule createDropSchemaRule;

    @Inject
    private TagService tagService;

    @Inject
    private TagRepository tagRepository;
    @Inject
    private EntityByTagRepository entityByTagRepository;

    @Test
    public void addTagsForEntity() {

        User user = TestUserUtil.prepareUser("login");
        StepVerifier.create(tagService.addTagsForEntity(newHashSet("tag1", "tag2"), user)).expectComplete().verify();

        StepVerifier.create(tagRepository.findAll()).assertNext(tag -> {
            Assert.assertNotNull(tag);
        }).assertNext(tag -> {
            Assert.assertNotNull(tag);
        }).verifyComplete();

        StepVerifier.create(entityByTagRepository.findAll()).assertNext(entityByTag -> {
            Assert.assertNotNull(entityByTag);
        }).assertNext(entityByTag -> {
            Assert.assertNotNull(entityByTag);
        }).verifyComplete();
    }

    @Test
    public void deleteTagsForEntity() {
        User user = TestUserUtil.prepareUser("login");
        tagService.addTagsForEntity(newHashSet("tag1", "tag2"), user).block();
        StepVerifier.create(tagService.deleteTagsForEntity(newHashSet("tag1"), user))
                .verifyComplete();

        StepVerifier.create(entityByTagRepository.findAllByPrimaryKeyTag("tag1")).verifyComplete();
        StepVerifier.create(entityByTagRepository.findAllByPrimaryKeyTag("tag2"))
                .assertNext(Assert::assertNotNull)
                .verifyComplete();
    }
}