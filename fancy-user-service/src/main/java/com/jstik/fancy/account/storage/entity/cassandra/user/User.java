package com.jstik.fancy.account.storage.entity.cassandra.user;


import com.jstik.fancy.account.model.user.IUser;
import com.jstik.fancy.account.storage.entity.EntityWithDiscriminator;
import com.jstik.fancy.account.storage.entity.cassandra.authority.Authority;
import lombok.*;
import org.springframework.data.annotation.Transient;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Table
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class User implements EntityWithDiscriminator, IUser {

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

    private boolean active;

    private String password;

    private Set<String> clients;

    private Set<String> tags;
    private Set<String> groups;

    private LocalDateTime created = LocalDateTime.now(Clock.systemUTC());

    @Transient
    private Collection<Authority> authorities = new ArrayList<>();

    public User(String login, String firstName, String lastName, @Email String email) {
        this.primaryKey = new UserPrimaryKey(login);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public String getLogin() {
        return getPrimaryKey().getLogin();
    }

    @Override
    public String getDiscriminator() {
        return User.class.getCanonicalName();
    }

    @Override
    public String getId() {
        return getPrimaryKey().getLogin();
    }

    public void addTags(Collection<String> tags) {
        if (this.getTags() == null)
            setTags(new HashSet<>());
        this.getTags().addAll(tags);
    }


    public void deleteTags(Collection<String> tags) {
        this.getTags().removeAll(tags);
    }

    public void addClients(Collection<String> clients) {
        if(this.clients == null)
            setClients(new HashSet<>());
        this.getClients().addAll(clients);
    }

    public void deleteClients(Collection<String> clients) {
        this.getClients().removeAll(clients);
    }


    public void addGroups(Collection<String> groups) {
        if(this.groups == null)
            setGroups(new HashSet<>());
        this.getGroups().addAll(groups);
    }

    public void addGroup(String group) {
        if(this.groups == null)
            setGroups(new HashSet<>());
        this.getGroups().add(group);
    }

    public void deleteGroups(Collection<String> groups) {
        this.getGroups().removeAll(groups);
    }


    @PrimaryKeyClass
    @NoArgsConstructor
    @Getter
    @Setter
    public static class UserPrimaryKey {

        @PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED)
        private String login;

      /*  @PrimaryKeyColumn(type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
        private LocalDateTime timestamp = LocalDateTime.now(Clock.systemUTC());*/

        public UserPrimaryKey(String login) {
            this.login = login;
        }
    }


}
