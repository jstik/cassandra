package com.jstik.fancy.account.service;

import com.google.common.collect.Sets;
import com.jstik.fancy.account.TestApp;
import com.jstik.fancy.account.dao.UserServiceCassandraConfig;
import com.jstik.fancy.account.dao.repository.EntityByTagRepository;
import com.jstik.fancy.account.dao.repository.TagRepository;
import com.jstik.fancy.account.entity.user.User;
import com.jstik.fancy.account.security.UserServiceSecurityConfig;
import com.jstik.fancy.account.web.UserServiceWebConfig;
import com.jstik.fancy.test.util.cassandra.CassandraCreateDropSchemaRule;
import com.jstik.fancy.test.util.cassandra.EmbeddedCassandraConfig;
import com.jstik.site.discovery.stub.StubLoadBalancerConfig;
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
import static org.junit.Assert.*;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringJUnitWebConfig
@ContextConfiguration(
        classes = {
                TestApp.class,
                EmbeddedCassandraConfig.class, UserServiceCassandraConfig.class,
                ServiceConfig.class, UserServiceWebConfig.class,
                UserServiceSecurityConfig.class,
                StubLoadBalancerConfig.class
        }
)

@TestPropertySource({"classpath:embedded-test.properties", "classpath:consul.properties"})
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