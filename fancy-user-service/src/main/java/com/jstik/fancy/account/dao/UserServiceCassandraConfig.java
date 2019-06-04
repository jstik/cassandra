package com.jstik.fancy.account.dao;

import com.jstik.site.cassandra.config.ReactiveCassandraConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.cassandra.repository.config.EnableReactiveCassandraRepositories;

@Configuration
@Import(ReactiveCassandraConfiguration.class)
@EnableReactiveCassandraRepositories({"com.jstik.fancy.account.dao.repository", "com.jstik.site.cassandra.repository"})
public class UserServiceCassandraConfig {
}
