package com.jstik.fancy.chat.dao.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractReactiveCassandraConfiguration;
import org.springframework.data.cassandra.config.CassandraClusterFactoryBean;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;
import org.springframework.data.cassandra.repository.config.EnableReactiveCassandraRepositories;

@Configuration
@EnableReactiveCassandraRepositories("com.jstik.fancy.chat.dao.repository")
public class CassandraConfig extends AbstractReactiveCassandraConfiguration {

    @Value("${cassandra.book.store.keyspace}")
    private String keyspace;

    @Value("${cassandra.cluster.contact.points}")
    private String points;

    @Value("${cassandra.cluster.port}")
    private Integer port;

    @Override
    protected String getKeyspaceName() {
        return keyspace;
    }

    @Bean
    public CassandraClusterFactoryBean cluster(){
        CassandraClusterFactoryBean cluster = new CassandraClusterFactoryBean();
        cluster.setContactPoints(points);
        cluster.setPort(port);
        return cluster;
    }

}
