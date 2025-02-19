package com.jstik.site.cassandra.statements;

import com.datastax.driver.core.RegularStatement;
import com.jstik.fancy.test.util.cassandra.CassandraCreateDropSchemaRule;
import com.jstik.fancy.test.util.cassandra.EmbeddedCassandraConfig;
import com.jstik.site.cassandra.test.TestCassandraConfiguration;
import com.jstik.site.cassandra.test.entity.TestTable;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.cassandra.core.ReactiveCassandraOperations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringJUnitWebConfig
@ContextConfiguration(
        classes = {
                EmbeddedCassandraConfig.class, TestCassandraConfiguration.class
        }
)
@TestPropertySource("classpath:embedded-test.properties")
public class DMLStatementProducerBuilderTest {

    @Inject
    private ReactiveCassandraOperations operations;

    @Rule
    @Inject
    public CassandraCreateDropSchemaRule createDropSchemaRule;

    @Test
    public void insertProducer() throws Exception {
        RegularStatement insert = DMLStatementProducerBuilder.insertProducer().apply(operations.getConverter(), prepareData());
        Boolean result = operations.getReactiveCqlOperations().execute(insert).block();
        Assert.assertTrue(result);

    }

    @Test
    public void insertIfNotExistsProducer() throws Exception {
        RegularStatement insert = DMLStatementProducerBuilder.insertIfNotExistsProducer().apply(operations.getConverter(), prepareData());
        Boolean result = operations.getReactiveCqlOperations().execute(insert).block();
        Assert.assertTrue(result);
    }

    @Test
    public void insertWithTTLProducer() throws Exception {
        RegularStatement insert = DMLStatementProducerBuilder.insertWithTTLProducer(20).apply(operations.getConverter(), prepareData());
        Boolean result = operations.getReactiveCqlOperations().execute(insert).block();
        Assert.assertTrue(result);
    }


    @Test
    public void updateByIdProducer() throws Exception {
        RegularStatement updateIfExist = DMLStatementProducerBuilder.updateByIdProducer(true).apply(operations.getConverter(), prepareData());
        //There is no such record in db so update should't be execute
        Assert.assertFalse(operations.getReactiveCqlOperations().execute(updateIfExist).block());

        RegularStatement update = DMLStatementProducerBuilder.updateByIdProducer(false).apply(operations.getConverter(), prepareData());
        Assert.assertTrue(operations.getReactiveCqlOperations().execute(update).block());
    }

    private TestTable prepareData(){
        TestTable testTable = new TestTable("key1");
        testTable.setTestData("Some Test");
        return testTable;
    }

}