package com.jstik.showcase.fancy.bookstore.dao.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
open class CassandraConfigImpl : CassandraConfig {

    @Value("\${cassandra.book.store.keyspace}")
    private lateinit var keyspace: String

    @Value("\${cassandra.cluster.contact.points}")
    private lateinit var points: String

    @Value("\${cassandra.cluster.port}")
    private val port: Int? = null


    override fun getPort(): Int {
        return port ?: throw IllegalStateException("Cassandra port property is null !")
    }

    override fun getKeyspaceName(): String {
        return keyspace
    }

    override fun getContactPoint(): String {
        return points
    }
}