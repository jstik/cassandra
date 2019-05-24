package com.jstik.showcase.fancy.bookstore.model

import org.springframework.data.cassandra.core.cql.Ordering
import org.springframework.data.cassandra.core.cql.PrimaryKeyType
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn
import java.io.Serializable
import java.util.*

@PrimaryKeyClass
data class BookPrimaryKey (@PrimaryKeyColumn(type = PrimaryKeyType.CLUSTERED, ordinal = 0) val title : String,
                           @PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED,
                                   value = "last_updated",
                                   ordinal = 0, ordering = Ordering.DESCENDING)  val lastUpdated : Date?,
                           @PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED, ordinal = 1) val author : String,
                           @PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED, ordinal = 2) val publisher : String?,
                           @PrimaryKeyColumn(name = "publish_date", type = PrimaryKeyType.PARTITIONED) val publishDate : Date?) : Serializable{
}