package com.jstik.fancy.util;

import org.junit.After;
import org.junit.Before;
import org.springframework.data.cassandra.core.CassandraAdminOperations;
import org.springframework.data.cassandra.core.mapping.CassandraMappingContext;

import javax.inject.Inject;
import java.util.HashMap;

public class EmbeddedCassandraEnvironment {

    @Inject
    private CassandraAdminOperations adminTemplate;
    @Inject
    private CassandraMappingContext cassandraMapping;

    @Before
    public void createTables() {
        cassandraMapping.getTableEntities().forEach(entity ->
                adminTemplate.createTable(true, entity.getTableName(), entity.getTypeInformation().getType(), new HashMap<>()));
    }


    @After
    public void dropTables() {
        cassandraMapping.getTableEntities().forEach(entity -> adminTemplate.dropTable(entity.getTableName()));
    }
}
