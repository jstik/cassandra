package com.jstik.showcase.fancy.bookstore.model

import org.springframework.data.cassandra.core.mapping.Column
import org.springframework.data.cassandra.core.mapping.PrimaryKey
import org.springframework.data.cassandra.core.mapping.Table

@Table("books")
data class Book(
        @PrimaryKey val bookKey: BookPrimaryKey
) {

    @Column
    var price: Double? = null

    var tags: MutableSet<String>? = null
}