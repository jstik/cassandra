package com.jstik.showcase.fancy.bookstore.endpoint

import com.jstik.showcase.fancy.bookstore.dao.repository.BookRepository
import com.jstik.showcase.fancy.bookstore.model.Book
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import javax.inject.Inject

@RestController
@RequestMapping("/books")
class BookEndpoint @Inject constructor(private val bookRepository: BookRepository) {

    @GetMapping("title/{title}")
    fun findAllByTitle(@PathVariable title: String) : Flux<Book> {
        return bookRepository.findByBookKeyTitle(title)
    }
}