package com.jstik.site.fancy.elastic.test;

import com.jstik.site.fancy.elastic.test.config.TestElasticConfig;
import com.jstik.site.fancy.elastic.test.dao.TestDocumentRepository;
import com.jstik.site.fancy.elastic.test.entity.TestDocument;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringJUnitWebConfig
@ContextConfiguration(
        classes = {
                TestElasticApp.class,
                EmbeddedElasticConfig.class,
                TestElasticConfig.class
        }
)
@TestPropertySource({"classpath:embedded-elasticsearch.properties"})
public class ElasticDropIndexesBeforeRuleTest {

    @Rule
    @Inject
    public ElasticDropIndexesBeforeRule elasticDropIndexesBeforeRule;

    @Inject
    TestDocumentRepository testDocumentRepository;

    @Test
    public void before() throws Exception {
        TestDocument document = new TestDocument();
        document.setId("1");
        document.setContent("content");
        testDocumentRepository.save(document);
    }

}