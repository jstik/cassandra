package com.jstik.fancy.util;

import com.jstik.fancy.chat.dao.config.ICassandraConfigProperties;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

@Getter
public class TestCassandraProperties implements ICassandraConfigProperties {

    @Value("${cassandra.book.store.keyspace}")
    private String keyspace;

    @Value("${cassandra.cluster.contact.points}")
    private String points;

    @Value("${cassandra.cluster.port}")
    private Integer port;

    @Value("${cassandra.config.file}")
    private String configurationFile;

}
