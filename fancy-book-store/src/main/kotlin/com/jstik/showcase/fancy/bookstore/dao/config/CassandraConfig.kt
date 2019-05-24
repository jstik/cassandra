package com.jstik.showcase.fancy.bookstore.dao.config

interface CassandraConfig {

   fun getKeyspaceName() : String
   fun getContactPoint() : String
   fun getPort() : Int;
}