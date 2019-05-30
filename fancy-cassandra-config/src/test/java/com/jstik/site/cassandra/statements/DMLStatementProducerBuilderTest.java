package com.jstik.site.cassandra.statements;

import com.datastax.driver.core.RegularStatement;
import com.jstik.fancy.test.util.cassandra.EmbeddedCassandraConfig;
import com.jstik.fancy.test.util.cassandra.EmbeddedCassandraEnvironment;
import com.jstik.site.cassandra.config.ReactiveCassandraConfiguration;
import com.jstik.site.cassandra.test.TestCassandraConfiguration;
import com.jstik.site.cassandra.test.entity.TestTable;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.cassandra.core.ReactiveCassandraOperations;
import org.springframework.data.cassandra.core.convert.CassandraConverter;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import reactor.core.publisher.Mono;

import javax.inject.Inject;

import java.util.function.BiFunction;

import static com.jstik.site.cassandra.statements.DMLStatementProducerBuilder.insertProducer;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringJUnitWebConfig
@ContextConfiguration(
        classes = {
                EmbeddedCassandraConfig.class, TestCassandraConfiguration.class
        }
)
@TestPropertySource("classpath:embedded-test.properties")
public class DMLStatementProducerBuilderTest extends EmbeddedCassandraEnvironment {

    @Inject
    private ReactiveCassandraOperations operations;

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
    public void insertProducer1() throws Exception {

    }

    @Test
    public void updateByIdProducer() throws Exception {
    }

    private TestTable prepareData(){
        TestTable testTable = new TestTable("key1");
        testTable.setTestData("Some Test");
        return testTable;
    }

}