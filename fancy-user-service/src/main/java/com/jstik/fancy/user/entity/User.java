package com.jstik.fancy.user.entity;


import lombok.*;
import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Set;

@Table
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class User {

    @PrimaryKey
    @NonNull
    @NotBlank
    private UserPrimaryKey primaryKey;

    @NonNull
    private String firstName;
    @NonNull
    private String lastName;

    @NonNull
    @Email
    private String email;
    private String password;
    private boolean active;
    private Set<String> clients;

    public User(String login, String firstName, String lastName, @Email String email) {
        this.primaryKey = new UserPrimaryKey(login);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public String getLogin(){
        return  getPrimaryKey().getLogin();
    }

    @PrimaryKeyClass
    @NoArgsConstructor
    @Getter
    @Setter
    public static class UserPrimaryKey{

        @PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED)
        private String login;

        @PrimaryKeyColumn(type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
        private LocalDateTime timestamp = LocalDateTime.now(Clock.systemUTC());

        public UserPrimaryKey(String login) {
            this.login = login;
        }
    }



}
