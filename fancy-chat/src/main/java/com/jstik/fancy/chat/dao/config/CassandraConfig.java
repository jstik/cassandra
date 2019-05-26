package com.jstik.fancy.chat.dao.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.cassandra.config.AbstractReactiveCassandraConfiguration;
import org.springframework.data.cassandra.config.CassandraClusterFactoryBean;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;
import org.springframework.data.cassandra.repository.config.EnableReactiveCassandraRepositories;

import javax.inject.Inject;

@Configuration
@EnableReactiveCassandraRepositories("com.jstik.fancy.chat.dao.repository")
public class CassandraConfig extends AbstractReactiveCassandraConfiguration {

    final ICassandraConfigProperties cassandraConfigProperties;

    public CassandraConfig(ICassandraConfigProperties cassandraConfigProperties) {
        this.cassandraConfigProperties = cassandraConfigProperties;
    }

    @Override
    protected String getKeyspaceName() {
        return cassandraConfigProperties.getKeyspace();
    }

    @Bean
    public CassandraClusterFactoryBean cluster(){
        CassandraClusterFactoryBean cluster = new CassandraClusterFactoryBean();
        cluster.setContactPoints(cassandraConfigProperties.getPoints());
        cluster.setPort(cassandraConfigProperties.getPort());
        return cluster;
    }

}
