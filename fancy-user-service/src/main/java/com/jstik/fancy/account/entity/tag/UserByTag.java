package com.jstik.fancy.account.entity.tag;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("user_by_tag")
@Getter
@Setter
public class UserByTag {

    @PrimaryKey
    private String tag;

    private String login;
}
