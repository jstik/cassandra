package com.jstik.fancy.account.storage.dao.repository.cassandra;


import com.jstik.site.cassandra.config.ReactiveCassandraConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.cassandra.repository.config.EnableReactiveCassandraRepositories;

@Configuration
@Import(ReactiveCassandraConfiguration.class)
@EnableReactiveCassandraRepositories({"com.jstik.fancy.account.storage.dao.repository.cassandra.*", "com.jstik.site.cassandra.repository"})
public class UserServiceCassandraConfig {

}
