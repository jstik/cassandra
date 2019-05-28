package com.jstik.fancy.user.entity;


import lombok.*;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

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
    private String login;

    private LocalDateTime created =  LocalDateTime.now(Clock.systemUTC());

    @Column("last_updated")
    private LocalDateTime lastUpdated = LocalDateTime.now(Clock.systemUTC());

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



}
