package com.jstik.showcase.fancy.bookstore.dao.repository

import com.jstik.showcase.fancy.bookstore.model.Book
import com.jstik.showcase.fancy.bookstore.model.BookPrimaryKey
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

@Repository
interface BookRepository : ReactiveCassandraRepository<Book, BookPrimaryKey> {

    fun findByBookKeyTitle(title : String) : Flux<Book>

}