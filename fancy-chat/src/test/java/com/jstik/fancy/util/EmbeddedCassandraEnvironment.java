package com.jstik.fancy.util;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import org.apache.thrift.transport.TTransportException;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.cassandra.core.CassandraAdminOperations;
import org.springframework.data.cassandra.core.mapping.CassandraMappingContext;

import javax.inject.Inject;
import java.io.IOException;
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
