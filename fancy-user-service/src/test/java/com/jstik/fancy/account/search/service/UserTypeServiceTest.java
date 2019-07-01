package com.jstik.fancy.account.search.service;

import com.google.common.collect.Sets;
import com.jstik.fancy.account.elastic.EmbeddedElasticConfig;
import com.jstik.fancy.account.search.TestElasticConfig;
import com.jstik.fancy.account.search.dao.repository.elastic.ElasticConfig;
import com.jstik.fancy.account.search.entity.elastic.UserType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringJUnitWebConfig
@ContextConfiguration(
        classes = {
                EmbeddedElasticConfig.class,
                TestElasticConfig.class,
                ElasticServiceConfig.class
        }
)
@TestPropertySource({"classpath:testElastic.properties"})
public class UserTypeServiceTest {

    @Inject UserTypeService userTypeService;

    @Test
    public void addUserDocument() throws Exception {

        UserType userType = new UserType("login", "first name", "last Name", "email@email.com");
        userType.setCreated(LocalDateTime.now());
        userType.setUpdated(LocalDateTime.now());
        userType.setTags(Sets.newHashSet("tag1", "tag2"));

        userTypeService.addUserDocument(userType);
    }

}