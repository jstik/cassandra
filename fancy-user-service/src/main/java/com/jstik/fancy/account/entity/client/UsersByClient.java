package com.jstik.fancy.account.entity.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@Getter
@Setter
@Table("users_by_client")
@NoArgsConstructor
@AllArgsConstructor
public class UsersByClient {

    @PrimaryKey
    private UsersByClientPrimaryKey primaryKey;

    public UsersByClient(String client, String login) {
        this.primaryKey = new UsersByClientPrimaryKey(client, login);
    }

    @Getter
    @Setter
    @PrimaryKeyClass
    @NoArgsConstructor
    public static class UsersByClientPrimaryKey{
        @PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED)
        private String client;

        @PrimaryKeyColumn(type = PrimaryKeyType.CLUSTERED)
        private String login;

        public UsersByClientPrimaryKey(String client, String login) {
            this.client = client;
            this.login = login;
        }
    }
}
