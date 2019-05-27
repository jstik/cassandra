package com.jstik.fancy.test.util.cassandra;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.cassandra.CassandraProperties;

@Getter
@Setter
public class TestCassandraProperties extends CassandraProperties {
    private String configurationFile;

}
