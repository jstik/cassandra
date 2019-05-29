package com.jstik.fancy.user.entity;

import lombok.*;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.UUID;

@Table("user_registration")
@Getter
@Setter
@NoArgsConstructor

public class UserRegistration {

    @PrimaryKey
    private UserRegistrationPrimaryKey primaryKey;

    public UserRegistration(String login, String key) {
        this.primaryKey = new UserRegistrationPrimaryKey(login, key);
    }

    @Getter
    @Setter
    @PrimaryKeyClass
    @NoArgsConstructor
    @RequiredArgsConstructor
    public static class UserRegistrationPrimaryKey{
        @PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED)
        @NonNull
        private String login;

        @PrimaryKeyColumn(type = PrimaryKeyType.CLUSTERED)
        @NonNull
        private String registrationKey;
    }
}
