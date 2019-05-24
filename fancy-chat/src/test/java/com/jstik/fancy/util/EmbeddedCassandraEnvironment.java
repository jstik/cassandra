package com.jstik.fancy.util;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import org.apache.thrift.transport.TTransportException;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
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


    @BeforeClass
    public static void startCassandraEmbedded() throws IOException, TTransportException, InterruptedException {
        EmbeddedCassandraServerHelper.startEmbeddedCassandra("test-cassandra.yaml");
        Cluster cluster = EmbeddedCassandraServerHelper.getCluster();
        Session session = cluster.connect();
        session.execute("CREATE KEYSPACE IF NOT EXISTS fancyChat WITH replication = { 'class': 'SimpleStrategy', 'replication_factor': '3' };");
        Thread.sleep(5000);
    }

    @Before
    public void createTables() {
        cassandraMapping.getTableEntities().forEach(entity ->
                adminTemplate.createTable(true, entity.getTableName(), entity.getTypeInformation().getType(), new HashMap<>()));
    }


    @After
    public void dropTables() {
        cassandraMapping.getTableEntities().forEach(entity -> adminTemplate.dropTable(entity.getTableName()));
    }

    @AfterClass
    public static void stopCassandraEmbedded() {
        EmbeddedCassandraServerHelper.cleanEmbeddedCassandra();
    }


}
