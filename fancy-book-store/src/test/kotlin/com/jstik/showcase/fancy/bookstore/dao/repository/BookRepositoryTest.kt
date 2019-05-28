package com.jstik.showcase.fancy.bookstore.dao.repository

import com.jstik.fancy.test.util.cassandra.EmbeddedCassandraConfig
import com.jstik.fancy.test.util.cassandra.EmbeddedCassandraEnvironment
import com.jstik.showcase.fancy.bookstore.dao.BookStoreCassandraConfig
import com.jstik.showcase.fancy.bookstore.model.Book
import com.jstik.showcase.fancy.bookstore.model.BookPrimaryKey
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import java.util.*
import javax.inject.Inject

@RunWith(SpringJUnit4ClassRunner::class)
@ContextConfiguration(classes = arrayOf(EmbeddedCassandraConfig::class, BookStoreCassandraConfig::class))
@TestPropertySource("classpath:embedded-test.properties")
class BookRepositoryTest  : EmbeddedCassandraEnvironment(){

    @Inject lateinit var bookRepository: BookRepository

    @Test
    fun createBookTest(){
        val book = Book(BookPrimaryKey("My Test Book", Date(), "Me", "My agency", Date()))
        book.price = 120.0
        book.tags = mutableSetOf("good book", "awesome")
        bookRepository.save(book)
    }

}