package com.jstik.fancy.account.entity.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Getter
@Setter
@Table("users_by_client")
@NoArgsConstructor
@AllArgsConstructor
public class UsersByClient {

    @PrimaryKey
    private String client;
    private String login;
}
