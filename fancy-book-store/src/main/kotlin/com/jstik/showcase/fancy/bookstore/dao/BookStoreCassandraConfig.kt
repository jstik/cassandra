package com.jstik.showcase.fancy.bookstore.dao
import com.jstik.showcase.fancy.bookstore.dao.config.CassandraConfig
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.cassandra.config.AbstractReactiveCassandraConfiguration
import org.springframework.data.cassandra.config.CassandraClusterFactoryBean
import org.springframework.data.cassandra.repository.config.EnableReactiveCassandraRepositories
import javax.inject.Inject


@Configuration
@EnableReactiveCassandraRepositories("com.jstik.showcase.fancy.bookstore.dao.repository")
@ComponentScan("com.jstik.showcase.fancy.bookstore.dao")
open class BookStoreCassandraConfig @Inject constructor(private val cassandraConfig : CassandraConfig): AbstractReactiveCassandraConfiguration() {

    override fun getKeyspaceName(): String {
       return cassandraConfig.getKeyspaceName()
    }

    @Bean
    override fun cluster(): CassandraClusterFactoryBean {
        val cluster = CassandraClusterFactoryBean()
        cluster.setContactPoints(cassandraConfig.getContactPoint())
        cluster.setPort(cassandraConfig.getPort())
        return cluster
    }


}