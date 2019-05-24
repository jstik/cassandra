package com.jstik.showcase.fancy.bookstore.dao.repository


import com.datastax.driver.core.Cluster
import com.jstik.showcase.fancy.bookstore.dao.config.CassandraConfig
import org.apache.cassandra.exceptions.ConfigurationException
import org.apache.thrift.transport.TTransportException
import org.cassandraunit.utils.EmbeddedCassandraServerHelper
import org.junit.After
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.cassandra.core.CassandraAdminOperations
import org.springframework.data.cassandra.core.mapping.CassandraMappingContext
import org.springframework.test.web.servlet.setup.MockMvcConfigurer
import java.io.IOException
import java.util.HashMap
import javax.inject.Inject
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy
import org.springframework.context.support.ResourceBundleMessageSource
import org.springframework.test.context.TestPropertySource

@Configuration
@TestPropertySource("classpath:test.properties")
open class EmbeddedCassandraEnvironment {
    @Inject
    private lateinit var adminTemplate: CassandraAdminOperations
    @Inject
    private lateinit var cassandraMapping: CassandraMappingContext

    @Inject
    lateinit var cassandraConfig: CassandraConfig
    @Value("\${cassandra.config.file}")
    lateinit var configFileName: String



    @Before
    @Throws(InterruptedException::class, TTransportException::class, ConfigurationException::class, IOException::class)
    fun startAndCreateTables() {
        cassandraMapping.tableEntities.onEach {
            adminTemplate.createTable(true, it.tableName, it.typeInformation.type, HashMap<String, Any>())
        }
    }


    @After
    fun dropTables() {
        cassandraMapping.tableEntities.onEach {
            adminTemplate.dropTable(it.tableName)
        }
    }

    companion object {
       // @BeforeClass
        @JvmStatic
        @Throws(InterruptedException::class, TTransportException::class, ConfigurationException::class, IOException::class)
        fun startCassandraEmbedded() {
            EmbeddedCassandraServerHelper.startEmbeddedCassandra("test-cassandra.yaml")
            val cluster = EmbeddedCassandraServerHelper.getCluster()
            val session = cluster.connect()
            session.execute("CREATE KEYSPACE IF NOT EXISTS fancyBookStore WITH replication = { 'class': 'SimpleStrategy', 'replication_factor': '3' };")
            Thread.sleep(5000)
        }

        @AfterClass
        @JvmStatic
        fun stopCassandraEmbedded() {
            EmbeddedCassandraServerHelper.cleanEmbeddedCassandra()
        }
    }

    protected fun startCassandraEmbedded(): Boolean {
        EmbeddedCassandraServerHelper.startEmbeddedCassandra(configFileName)
        val cluster = EmbeddedCassandraServerHelper.getCluster()
        val session = cluster.connect()
        session.execute("CREATE KEYSPACE IF NOT EXISTS ${cassandraConfig.getKeyspaceName()} WITH replication = { 'class': 'SimpleStrategy', 'replication_factor': '3' };")
        Thread.sleep(5000)
        return true
    }

    @Bean
    @Lazy(false)
    open fun propertiesResolver(cassandraConfig: CassandraConfig, @Value("\${cassandra.config.file}")  configFileName: String): Cluster {
        EmbeddedCassandraServerHelper.startEmbeddedCassandra(configFileName)
        val cluster = EmbeddedCassandraServerHelper.getCluster()
        val session = cluster.connect()
        session.execute("CREATE KEYSPACE IF NOT EXISTS fancyBookStore WITH replication = { 'class': 'SimpleStrategy', 'replication_factor': '3' };")
        Thread.sleep(5000)
        return cluster
    }




}
