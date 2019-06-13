package com.jstik.site.cassandra.config.keyspace;


import com.jstik.site.cassandra.test.KeyspacePropertiesTestConfiguration;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.cassandra.config.KeyspaceAction;
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;

import static com.jstik.site.cassandra.config.keyspace.KeyspaceSpecificationBuilder.from;
import static org.hamcrest.core.Is.is;
import static org.springframework.data.cassandra.core.cql.keyspace.KeyspaceOption.DURABLE_WRITES;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringJUnitWebConfig
@ContextConfiguration(classes = {KeyspacePropertiesTestConfiguration.class})
@TestPropertySource("classpath:keyspace-specification-factory-test.properties")
public class KeyspaceSpecificationFactoryTest {


    @Inject
    private KeyspaceProperties keyspaceProperties;
    private static final String keyspaceName = "testKeyspace";

    @Test
    public void create() throws Exception {
        KeyspaceSpecificationBuilder builder = from(keyspaceName);
        KeyspaceSpecificationFactory factory = builder.build(keyspaceProperties);
        CreateKeyspaceSpecification specification = factory.create();
        Assert.assertNotNull(specification);
        Assert.assertTrue(specification.getIfNotExists());
        Assert.assertTrue(specification.getOptions().containsKey(DURABLE_WRITES.getName()));
        Assert.assertTrue(specification.getOptions().get(DURABLE_WRITES.getName()).equals(true));

    }

    @Test
    public void alter() throws Exception {
        KeyspaceSpecificationBuilder builder = from(keyspaceName);
        KeyspaceSpecificationFactory factory = builder.build(keyspaceProperties);
        factory.alter();
    }

    @Test
    public void drop() throws Exception {
        KeyspaceSpecificationBuilder builder = from(keyspaceName);
        KeyspaceSpecificationFactory factory = builder.build(keyspaceProperties);
        Assert.assertNotNull(factory.drop());
    }

    @Test
    public void createSpecificationsOrEmpty() throws Exception {
        KeyspaceSpecificationFactory forCreateDrop = from(keyspaceName).build(propertiesKeyspaceAction(KeyspaceAction.CREATE_DROP));
        Assert.assertThat(forCreateDrop.createSpecificationsOrEmpty().size(), is(1));
        KeyspaceSpecificationFactory forNone = from(keyspaceName).build(propertiesKeyspaceAction(KeyspaceAction.NONE));
        Assert.assertTrue(forNone.createSpecificationsOrEmpty().isEmpty());
        KeyspaceSpecificationFactory forAlter = from(keyspaceName).build(propertiesKeyspaceAction(KeyspaceAction.ALTER));
        Assert.assertTrue(forAlter.createSpecificationsOrEmpty().isEmpty());
        KeyspaceSpecificationFactory forCreate = from(keyspaceName).build(propertiesKeyspaceAction(KeyspaceAction.CREATE));
        Assert.assertThat(forCreate.createSpecificationsOrEmpty().size(), is(1));

    }

    @Test
    public void dropSpecificationsOrEmpty() throws Exception {
        KeyspaceSpecificationFactory forCreateDrop = from(keyspaceName).build(propertiesKeyspaceAction(KeyspaceAction.CREATE_DROP));
        Assert.assertThat(forCreateDrop.dropSpecificationsOrEmpty().size(), is(1));
        KeyspaceSpecificationFactory forNone = from(keyspaceName).build(propertiesKeyspaceAction(KeyspaceAction.NONE));
        Assert.assertTrue(forNone.dropSpecificationsOrEmpty().isEmpty());
        KeyspaceSpecificationFactory forAlter = from(keyspaceName).build(propertiesKeyspaceAction(KeyspaceAction.ALTER));
        Assert.assertTrue(forAlter.dropSpecificationsOrEmpty().isEmpty());
        KeyspaceSpecificationFactory forCreate = from(keyspaceName).build(propertiesKeyspaceAction(KeyspaceAction.CREATE));
        Assert.assertTrue(forCreate.dropSpecificationsOrEmpty().isEmpty());
    }


    private KeyspaceProperties propertiesKeyspaceAction(KeyspaceAction action) {
        KeyspaceProperties properties = new KeyspaceProperties();
        properties.setKeyspaceAction(action);
        return properties;
    }

}