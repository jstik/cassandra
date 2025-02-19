package com.jstik.site.cassandra.test;

import com.jstik.site.cassandra.config.ReactiveCassandraConfiguration;
import com.jstik.site.cassandra.config.setup.ICassandraSetupService;
import org.springframework.boot.autoconfigure.cassandra.CassandraProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.CassandraEntityClassScanner;

import java.util.Set;

@Configuration

@ComponentScan("com.jstik.site.cassandra.test")
public class TestCassandraConfiguration  extends ReactiveCassandraConfiguration{


    public TestCassandraConfiguration(CassandraProperties cassandraProperties, ICassandraSetupService setupService) {
        super(cassandraProperties, setupService);
    }


    protected Set<Class<?>> getInitialEntitySet() throws ClassNotFoundException {
        Set<Class<?>> entitySet = super.getInitialEntitySet();
        entitySet.addAll(CassandraEntityClassScanner.scan("com.jstik.site.cassandra.test.*"));
        return entitySet;
    }
}
