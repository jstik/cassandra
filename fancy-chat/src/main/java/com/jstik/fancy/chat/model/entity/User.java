package com.jstik.fancy.chat.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("users")
@Getter
@Setter
public class User {

    @PrimaryKey
    private String login;

    @Column("telephone_number")
    private Long telephoneNumber;


    private String password;


    private String name;


    private String email;

    @Column("photo_link")
    private String photoLink;

}
