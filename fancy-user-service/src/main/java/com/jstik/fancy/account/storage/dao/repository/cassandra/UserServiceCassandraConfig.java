package com.jstik.fancy.account.storage.dao.repository.cassandra;


import com.jstik.site.cassandra.config.ReactiveCassandraConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.cassandra.core.CassandraAdminOperations;
import org.springframework.data.cassandra.core.CassandraPersistentEntitySchemaCreator;
import org.springframework.data.cassandra.core.mapping.CassandraMappingContext;
import org.springframework.data.cassandra.repository.config.EnableReactiveCassandraRepositories;

@Configuration
@Import(ReactiveCassandraConfiguration.class)
@EnableReactiveCassandraRepositories({"com.jstik.fancy.account.storage.dao.repository.cassandra.*", "com.jstik.site.cassandra.repository"})
public class UserServiceCassandraConfig {


    private CassandraAdminOperations adminTemplate;

    private CassandraMappingContext cassandraMapping;

    public UserServiceCassandraConfig(CassandraAdminOperations adminTemplate, CassandraMappingContext cassandraMapping) {
        this.adminTemplate = adminTemplate;
        this.cassandraMapping = cassandraMapping;

        CassandraPersistentEntitySchemaCreator schemaCreator = new CassandraPersistentEntitySchemaCreator(cassandraMapping, adminTemplate);
        schemaCreator.createUserTypes(true);
        schemaCreator.createTables(true);
        schemaCreator.createIndexes(true);
    }
}
