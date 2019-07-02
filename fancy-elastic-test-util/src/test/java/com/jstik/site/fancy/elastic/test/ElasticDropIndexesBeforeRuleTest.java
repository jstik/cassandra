package com.jstik.site.fancy.elastic.test;

import com.jstik.site.fancy.elastic.test.config.TestElasticConfig;
import com.jstik.site.fancy.elastic.test.dao.TestDocumentRepository;
import com.jstik.site.fancy.elastic.test.entity.TestDocument;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;

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
    private TestDocumentRepository testDocumentRepository;

    @Inject
    private ElasticsearchOperations elasticsearchOperations;



    @Test
    public void before() throws Exception {
        //elasticDropIndexesBeforeRule is used as rule that delete indexes before each test so indexes shouldn't exist
        Assert.assertFalse(elasticsearchOperations.indexExists(TestDocument.class));
        TestDocument document = new TestDocument();
        document.setId("1");
        document.setContent("content");
        testDocumentRepository.save(document);

        //indexes were created on the fly
        Assert.assertTrue(elasticsearchOperations.indexExists(TestDocument.class));

        //drop indexes again
        elasticDropIndexesBeforeRule.before();

        //finally verify that index doesn't exist
        Assert.assertFalse(elasticsearchOperations.indexExists(TestDocument.class));

    }

}