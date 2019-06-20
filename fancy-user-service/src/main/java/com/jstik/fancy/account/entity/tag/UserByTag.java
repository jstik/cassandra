package com.jstik.fancy.account.entity.tag;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("user_by_tag")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserByTag {

    @PrimaryKey
    private UserByTagPrimaryKey primaryKey;


    public UserByTag(String tag, String login) {
        this.primaryKey = new UserByTagPrimaryKey(tag, login);
    }

    @Getter
    @Setter
    @PrimaryKeyClass
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserByTagPrimaryKey{
        @PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED)
        private String tag;

        @PrimaryKeyColumn(type = PrimaryKeyType.CLUSTERED)
        private String login;
    }
}
