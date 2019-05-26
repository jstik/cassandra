package com.jstik.fancy.util;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import org.apache.thrift.transport.TTransportException;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.io.IOException;

@Configuration
public class EmbeddedCassandraConfig implements DisposableBean {

    @Bean
    @Lazy(false)
    public CommandLineRunner commandLineRunner(TestCassandraProperties cassandraConfigProperties) throws IOException, TTransportException, InterruptedException {
        EmbeddedCassandraServerHelper.startEmbeddedCassandra(cassandraConfigProperties.getConfigurationFile());
        Cluster cluster = EmbeddedCassandraServerHelper.getCluster();
        Session session = cluster.connect();
        session.execute("CREATE KEYSPACE IF NOT EXISTS " + cassandraConfigProperties.getKeyspace() +" WITH replication = { 'class': 'SimpleStrategy', 'replication_factor': '3' };");
        Thread.sleep(5000);
        return args -> {};
    }

    @Bean
    public TestCassandraProperties cassandraConfigProperties(){
        return  new TestCassandraProperties() ;
    }


    @Override
    public void destroy() {
        EmbeddedCassandraServerHelper.cleanEmbeddedCassandra();
    }
}
