package com.jstik.fancy.account.storage.entity.cassandra.user;

import lombok.*;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

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

    public String getRegKey() {
        if (primaryKey == null)
            return null;
        return primaryKey.getRegistrationKey();
    }

    public String getLogin(){
        if (primaryKey == null)
            return null;
        return primaryKey.getLogin();
    }

    @Getter
    @Setter
    @PrimaryKeyClass
    @NoArgsConstructor
    @RequiredArgsConstructor
    public static class UserRegistrationPrimaryKey {
        @PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED)
        @NonNull
        private String login;

        @PrimaryKeyColumn(type = PrimaryKeyType.CLUSTERED)
        @NonNull
        private String registrationKey;
    }
}
