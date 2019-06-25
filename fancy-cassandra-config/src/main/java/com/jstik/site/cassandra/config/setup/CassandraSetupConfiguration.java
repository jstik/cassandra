package com.jstik.site.cassandra.config.setup;

import com.jstik.site.cassandra.config.setup.keyspace.KeyspaceProperties;
import com.jstik.site.cassandra.config.setup.script.CassandraScriptsProperties;
import com.jstik.site.cassandra.convertors.StringToEnumConverterFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.io.Resource;

@Configuration
public class CassandraSetupConfiguration {

    @Value("${#{scriptsProperties.getStartupScript}:classpath:cassandra-startup.sql}")
    private Resource startupScript;

    @Value("${#{scriptsProperties.getShutdownScript}:classpath:cassandra-shutdown.sql}")
    private Resource shutdownScript;

    @Bean
    @ConfigurationProperties(prefix = "spring.data.cassandra.custom.script")
    public CassandraScriptsProperties scriptsProperties() {
        return new CassandraScriptsProperties();
    }


    @Bean("customCassandraConversionService")
    public ConversionService conversionService() {
        DefaultConversionService service = new DefaultConversionService();
        service.addConverterFactory(new StringToEnumConverterFactory());
        return service;
    }


    @Bean
    @ConfigurationProperties(prefix = "spring.data.cassandra.custom.keyspace")
    public KeyspaceProperties keyspaceProperties() {
        return new KeyspaceProperties();
    }



    @Bean
    public ICassandraSetupService cassandraSetupService() {
        return new CassandraSetupService(keyspaceProperties(), conversionService(), startupScript, shutdownScript);
    }


}
