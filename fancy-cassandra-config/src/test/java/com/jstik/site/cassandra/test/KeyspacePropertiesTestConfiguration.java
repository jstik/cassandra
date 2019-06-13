package com.jstik.site.cassandra.test;

import com.jstik.site.cassandra.config.keyspace.KeyspaceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
public class KeyspacePropertiesTestConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "spring.data.cassandra.custom.keyspace")
    public KeyspaceProperties keyspaceProperties(){
        return new KeyspaceProperties();
    }

}
