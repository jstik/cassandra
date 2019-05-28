package com.jstik.showcase.fancy.bookstore.dao
import com.jstik.site.cassandra.config.ReactiveCassandraConfiguration
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.data.cassandra.repository.config.EnableReactiveCassandraRepositories



@Configuration
@Import(ReactiveCassandraConfiguration::class)
@EnableReactiveCassandraRepositories("com.jstik.showcase.fancy.bookstore.dao.repository")
open class BookStoreCassandraConfig{


}