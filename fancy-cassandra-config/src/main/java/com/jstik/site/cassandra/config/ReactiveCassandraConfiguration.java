package com.jstik.site.cassandra.config;

import com.jstik.site.cassandra.config.script.ICassandraSetupService;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.cassandra.CassandraProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.cassandra.config.AbstractReactiveCassandraConfiguration;
import org.springframework.data.cassandra.config.CassandraClusterFactoryBean;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification;
import org.springframework.data.cassandra.core.cql.keyspace.DropKeyspaceSpecification;

import java.util.List;

@Configuration
@Import(CassandraSetupConfiguration.class)
public class ReactiveCassandraConfiguration extends AbstractReactiveCassandraConfiguration {

    private final CassandraProperties cassandraProperties;
    private ICassandraSetupService setupService;


    public ReactiveCassandraConfiguration(CassandraProperties cassandraProperties, ICassandraSetupService setupService) {
        this.cassandraProperties = cassandraProperties;
        this.setupService = setupService;
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
        return setupService.getKeyspaceCreations(getKeyspaceName());
    }

    @NotNull
    @Override
    protected List<DropKeyspaceSpecification> getKeyspaceDrops() {
        return setupService.getKeyspaceDrops(getKeyspaceName());
    }


    @NotNull
    @Override
    public SchemaAction getSchemaAction() {
       return setupService.getSchemaAction(cassandraProperties.getSchemaAction());
    }


    @NotNull
    @Override
    protected List<String> getStartupScripts() {
        return setupService.getStartupScripts();
    }

    @NotNull
    @Override
    protected List<String> getShutdownScripts() {
        return setupService.getShutdownScripts();
    }
}
