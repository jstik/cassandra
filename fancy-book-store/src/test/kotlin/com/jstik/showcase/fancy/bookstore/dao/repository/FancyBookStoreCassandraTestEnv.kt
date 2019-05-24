package com.jstik.showcase.fancy.bookstore.dao.repository

import org.apache.commons.logging.LogFactory
import org.apache.cassandra.exceptions.ConfigurationException
import org.cassandraunit.utils.EmbeddedCassandraServerHelper
import java.io.IOException
import org.apache.thrift.transport.TTransportException
import org.junit.BeforeClass
import com.jstik.showcase.fancy.bookstore.model.Book
import org.springframework.data.cassandra.core.cql.CqlIdentifier
import org.junit.Before
import org.springframework.data.cassandra.core.CassandraAdminOperations
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.cassandra.core.mapping.Table
import org.junit.AfterClass
import org.junit.After
import org.springframework.data.cassandra.core.mapping.CassandraMappingContext
import kotlin.reflect.KClass
import java.util.*


open class FancyBookStoreCassandraTestEnv {

    @Autowired
    private lateinit var  adminTemplate: CassandraAdminOperations
    @Autowired
    private lateinit var cassandraMapping : CassandraMappingContext;

    companion object{

        val log = LogFactory.getLog(FancyBookStoreCassandraTestEnv::class.java)
        @BeforeClass @JvmStatic
        @Throws(InterruptedException::class, TTransportException::class, ConfigurationException::class, IOException::class)
        fun startCassandraEmbedded() {

            EmbeddedCassandraServerHelper.startEmbeddedCassandra("test-cassandra.yaml")

            val cluster = EmbeddedCassandraServerHelper.getCluster()

            val session = cluster.connect()
            session.execute("CREATE KEYSPACE IF NOT EXISTS fancyBookStore WITH replication = { 'class': 'SimpleStrategy', 'replication_factor': '3' };")
            Thread.sleep(5000)


        }

        @AfterClass @JvmStatic
        fun stopCassandraEmbedded() {
            EmbeddedCassandraServerHelper.cleanEmbeddedCassandra()
        }


    }


    @Before
    @Throws(InterruptedException::class, TTransportException::class, ConfigurationException::class, IOException::class)
    fun createTable() {
        cassandraMapping.tableEntities.onEach {
            adminTemplate.createTable(true, it.tableName, it.typeInformation.type, HashMap<String, Any>())
        }
    }


    @After
    fun dropTable() {
        adminTemplate.dropTable(CqlIdentifier.of(findEntityName(Book::class)))
    }


    private fun findEntityName(entityClass : KClass<*>) : String?{
        val table = entityClass.annotations.find { it is Table } as Table?;
        return  table?.value ?: entityClass.simpleName
    }
}
