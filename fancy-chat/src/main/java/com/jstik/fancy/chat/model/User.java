package com.jstik.fancy.chat.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("users")
@Getter
@Setter
public class User {

    @PrimaryKeyColumn(type = PrimaryKeyType.CLUSTERED)
    private String login;

    @PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED)
    private Long telephoneNumber;

    private String password;
    private String name;
    private String email;
    private String photoLink;

}
