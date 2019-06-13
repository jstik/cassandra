package com.jstik.site.cassandra.config;

import com.jstik.site.cassandra.config.keyspace.KeyspaceProperties;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.cassandra.CassandraProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractReactiveCassandraConfiguration;
import org.springframework.data.cassandra.config.CassandraClusterFactoryBean;
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification;
import org.springframework.data.cassandra.core.cql.keyspace.DropKeyspaceSpecification;

import java.util.List;

import static com.jstik.site.cassandra.config.keyspace.KeyspaceSpecificationBuilder.from;

@Configuration
public class ReactiveCassandraConfiguration extends AbstractReactiveCassandraConfiguration {

    private final CassandraProperties cassandraProperties;


    public ReactiveCassandraConfiguration(CassandraProperties cassandraProperties) {
        this.cassandraProperties = cassandraProperties;
    }

    @NotNull
    @Override
    protected String getKeyspaceName() {
        return cassandraProperties.getKeyspaceName();
    }

    @NotNull
    @Bean
    public CassandraClusterFactoryBean cluster(){
        CassandraClusterFactoryBean cluster = super.cluster();
        cluster.setContactPoints(String.join(",", cassandraProperties.getContactPoints()));
        cluster.setPort(cassandraProperties.getPort());
        return cluster;
    }

    @NotNull
    @Override
    protected List<CreateKeyspaceSpecification> getKeyspaceCreations() {
        return from(getKeyspaceName()).build(keyspaceProperties()).createSpecificationsOrEmpty();
    }

    @NotNull
    @Override
    protected List<DropKeyspaceSpecification> getKeyspaceDrops() {
        return from(getKeyspaceName()).build(keyspaceProperties()).dropSpecificationsOrEmpty();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.data.cassandra.custom.keyspace")
    public KeyspaceProperties keyspaceProperties(){
        return new KeyspaceProperties();
    }

}
