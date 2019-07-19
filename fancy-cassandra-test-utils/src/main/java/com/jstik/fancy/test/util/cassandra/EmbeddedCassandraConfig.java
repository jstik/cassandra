package com.jstik.fancy.test.util.cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import me.prettyprint.hector.api.exceptions.HectorException;
import org.apache.thrift.transport.TTransportException;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.data.cassandra.core.CassandraAdminOperations;
import org.springframework.data.cassandra.core.mapping.CassandraMappingContext;

import java.io.IOException;

@Configuration
@EnableConfigurationProperties({TestCassandraProperties.class})
public class EmbeddedCassandraConfig implements DisposableBean {

    Logger logger = LoggerFactory.getLogger(EmbeddedCassandraConfig.class);

    public EmbeddedCassandraConfig(TestCassandraProperties cassandraConfigProperties) {
        try {
            EmbeddedCassandraServerHelper.startEmbeddedCassandra(cassandraConfigProperties.getConfigurationFile(), cassandraConfigProperties.getTimeout());
            Cluster cluster = EmbeddedCassandraServerHelper.getCluster();
            Session session = cluster.connect();
            session.execute("CREATE KEYSPACE IF NOT EXISTS " + cassandraConfigProperties.getKeyspaceName() +" WITH replication = { 'class': 'SimpleStrategy', 'replication_factor': '1' };");
        } catch (Exception e ) {
            logger.error("Couldn't startup embedded cassandra", e);
        }

    }
/*
   // @Bean
    @Lazy(false)
    public CommandLineRunner commandLineRunner(TestCassandraProperties cassandraConfigProperties) throws IOException, TTransportException, InterruptedException {
        EmbeddedCassandraServerHelper.startEmbeddedCassandra(cassandraConfigProperties.getConfigurationFile());
        Cluster cluster = EmbeddedCassandraServerHelper.getCluster();
        Session session = cluster.connect();
        session.execute("CREATE KEYSPACE IF NOT EXISTS " + cassandraConfigProperties.getKeyspaceName() +" WITH replication = { 'class': 'SimpleStrategy', 'replication_factor': '1' };");
        Thread.sleep(5000);
        return args -> {};
    }*/

    @Bean
    @Primary
    public TestCassandraProperties cassandraConfigProperties(){
        return  new TestCassandraProperties() ;
    }


    @Override
    public void destroy() {
        try {
            EmbeddedCassandraServerHelper.cleanEmbeddedCassandra();
        }catch (HectorException e){
            logger.debug(" Embedded Cassandra Server seems already down ", e);
        }

    }

    @Bean
    public CassandraCreateDropSchemaRule createDropSchemaRule(CassandraAdminOperations adminTemplate, CassandraMappingContext cassandraMapping){
       return  new  CassandraCreateDropSchemaRule(adminTemplate, cassandraMapping);
    }
}
