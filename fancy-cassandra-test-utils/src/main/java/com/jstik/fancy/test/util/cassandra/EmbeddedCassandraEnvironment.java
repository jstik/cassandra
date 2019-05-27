package com.jstik.fancy.test.util.cassandra;

import org.junit.After;
import org.junit.Before;
import org.springframework.data.cassandra.core.CassandraAdminOperations;
import org.springframework.data.cassandra.core.CassandraPersistentEntitySchemaCreator;
import org.springframework.data.cassandra.core.CassandraPersistentEntitySchemaDropper;
import org.springframework.data.cassandra.core.mapping.CassandraMappingContext;

import javax.inject.Inject;

public class EmbeddedCassandraEnvironment {

    @Inject
    private CassandraAdminOperations adminTemplate;
    @Inject
    private CassandraMappingContext cassandraMapping;

    @Before
    public void createSchema() {
        CassandraPersistentEntitySchemaCreator schemaCreator = new CassandraPersistentEntitySchemaCreator(cassandraMapping, adminTemplate);
        schemaCreator.createUserTypes(true);
        schemaCreator.createTables(true);
        schemaCreator.createIndexes(true);
    }


    @After
    public void dropSchema() {
        CassandraPersistentEntitySchemaDropper dropper = new CassandraPersistentEntitySchemaDropper(cassandraMapping, adminTemplate);
        dropper.dropTables(false);
        dropper.dropUserTypes(false);
    }
}
