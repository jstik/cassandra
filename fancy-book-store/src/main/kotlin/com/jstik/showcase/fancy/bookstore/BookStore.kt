package com.jstik.showcase.fancy.bookstore


import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
open class BookStore

fun main(args: Array<String>){
    SpringApplication.run(BookStore::class.java, *args)
}