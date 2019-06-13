package com.jstik.site.cassandra.config.keyspace;

import com.jstik.fancy.test.util.cassandra.EmbeddedCassandraConfig;
import com.jstik.site.cassandra.test.TestCassandraConfiguration;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.cassandra.CassandraProperties;
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;

import static org.springframework.data.cassandra.core.cql.keyspace.KeyspaceOption.DURABLE_WRITES;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringJUnitWebConfig
@ContextConfiguration(
        classes = {
                EmbeddedCassandraConfig.class, TestCassandraConfiguration.class
        }
)
@TestPropertySource("classpath:test.properties")
public class KeyspaceSpecificationFactoryTest {

    @Inject private KeyspaceProperties keyspaceProperties;
    @Inject private CassandraProperties cassandraProperties;

    @Test
    public void create() throws Exception {
        KeyspaceSpecificationBuilder builder = KeyspaceSpecificationBuilder.from(cassandraProperties.getKeyspaceName());
        KeyspaceSpecificationFactory factory = builder.build(keyspaceProperties);
        CreateKeyspaceSpecification specification = factory.create();
        Assert.assertNotNull(specification);
        Assert.assertTrue(specification.getIfNotExists());
        Assert.assertTrue(specification.getOptions().containsKey(DURABLE_WRITES.getName()));
        Assert.assertTrue(specification.getOptions().get(DURABLE_WRITES.getName()).equals(true));

    }

    @Test
    public void alter() throws Exception {
        KeyspaceSpecificationBuilder builder = KeyspaceSpecificationBuilder.from(cassandraProperties.getKeyspaceName());
        KeyspaceSpecificationFactory factory = builder.build(keyspaceProperties);
        factory.alter();
    }

    @Test
    public void drop() throws Exception {
        KeyspaceSpecificationBuilder builder = KeyspaceSpecificationBuilder.from(cassandraProperties.getKeyspaceName());
        KeyspaceSpecificationFactory factory = builder.build(keyspaceProperties);
        Assert.assertNotNull(factory.drop());
    }

}