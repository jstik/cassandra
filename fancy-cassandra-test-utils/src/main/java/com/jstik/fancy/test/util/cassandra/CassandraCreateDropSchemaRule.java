package com.jstik.fancy.test.util.cassandra;

import org.junit.rules.ExternalResource;
import org.springframework.data.cassandra.core.CassandraAdminOperations;
import org.springframework.data.cassandra.core.CassandraPersistentEntitySchemaCreator;
import org.springframework.data.cassandra.core.CassandraPersistentEntitySchemaDropper;
import org.springframework.data.cassandra.core.mapping.CassandraMappingContext;


public class CassandraCreateDropSchemaRule  extends ExternalResource {

    private CassandraAdminOperations adminTemplate;

    private CassandraMappingContext cassandraMapping;

    public CassandraCreateDropSchemaRule(CassandraAdminOperations adminTemplate, CassandraMappingContext cassandraMapping) {
        this.adminTemplate = adminTemplate;
        this.cassandraMapping = cassandraMapping;
    }

    @Override
    protected void before() throws Throwable {
        CassandraPersistentEntitySchemaCreator schemaCreator = new CassandraPersistentEntitySchemaCreator(cassandraMapping, adminTemplate);
        schemaCreator.createUserTypes(true);
        schemaCreator.createTables(true);
        schemaCreator.createIndexes(true);
    }

    @Override
    protected void after() {
        CassandraPersistentEntitySchemaDropper dropper = new CassandraPersistentEntitySchemaDropper(cassandraMapping, adminTemplate);
        dropper.dropTables(false);
        dropper.dropUserTypes(false);
    }
}
