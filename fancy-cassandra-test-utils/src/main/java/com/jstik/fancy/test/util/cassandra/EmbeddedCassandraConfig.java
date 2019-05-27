package com.jstik.fancy.test.util.cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import org.apache.thrift.transport.TTransportException;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;

import java.io.IOException;

@Configuration
@EnableConfigurationProperties({TestCassandraProperties.class})
public class EmbeddedCassandraConfig implements DisposableBean {

    @Bean
    @Lazy(false)
    public CommandLineRunner commandLineRunner(TestCassandraProperties cassandraConfigProperties) throws IOException, TTransportException, InterruptedException {
        EmbeddedCassandraServerHelper.startEmbeddedCassandra(cassandraConfigProperties.getConfigurationFile());
        Cluster cluster = EmbeddedCassandraServerHelper.getCluster();
        Session session = cluster.connect();
        session.execute("CREATE KEYSPACE IF NOT EXISTS " + cassandraConfigProperties.getKeyspaceName() +" WITH replication = { 'class': 'SimpleStrategy', 'replication_factor': '3' };");
        Thread.sleep(5000);
        return args -> {};
    }

    @Bean
    @Primary
    public TestCassandraProperties cassandraConfigProperties(){
        return  new TestCassandraProperties() ;
    }


    @Override
    public void destroy() {
        EmbeddedCassandraServerHelper.cleanEmbeddedCassandra();
    }
}
