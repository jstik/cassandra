package com.jstik.fancy.account.storage.entity.cassandra.tag;

import com.datastax.driver.core.DataType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table
@Getter
@Setter
public class Tag {

    @PrimaryKey
    private String name;

    @CassandraType(type= DataType.Name.COUNTER)
    private Long counter;
}
