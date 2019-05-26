package com.jstik.fancy.chat.dao.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class CassandraConfigProperties implements ICassandraConfigProperties {

    @Value("${cassandra.book.store.keyspace}")
    private String keyspace;

    @Value("${cassandra.cluster.contact.points}")
    private String points;

    @Value("${cassandra.cluster.port}")
    private Integer port;


}
