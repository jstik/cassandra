package com.jstik.fancy.chat.dao.config;

import com.jstik.site.cassandra.config.ReactiveCassandraConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.cassandra.repository.config.EnableReactiveCassandraRepositories;

@Configuration
@Import(ReactiveCassandraConfiguration.class)
@EnableReactiveCassandraRepositories("com.jstik.fancy.chat.dao.repository")
public class CassandraConfig {
}
