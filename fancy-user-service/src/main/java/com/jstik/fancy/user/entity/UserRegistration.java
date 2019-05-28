package com.jstik.fancy.user.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("user_registration")
@Getter
@Setter
public class UserRegistration {

    @PrimaryKey
    private UserRegistrationPrimaryKey primaryKey;

    @Getter
    @Setter
    public static class UserRegistrationPrimaryKey{
        @PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED)
        private String login;
        @PrimaryKeyColumn(type = PrimaryKeyType.CLUSTERED)
        private String registrationKey;
    }
}
