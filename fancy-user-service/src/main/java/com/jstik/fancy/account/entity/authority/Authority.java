package com.jstik.fancy.account.entity.authority;

import com.jstik.fancy.account.entity.user.User;
import com.jstik.fancy.account.model.AccessLevel;
import com.jstik.fancy.account.model.AuthorityDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Table("authority")
@NoArgsConstructor
@AllArgsConstructor
public class Authority {

    @PrimaryKey
    private AuthorityPrimaryKey key;

    @Column("access_level")
    private AccessLevel accessLevel;

    public Authority(@NotNull AuthorityDTO auth, @NotNull User user){
        this.key = new AuthorityPrimaryKey(user.getLogin(), AuthorityType.USER, auth.getClient(), auth.getAuthority());
        this.accessLevel = auth.getAccessLevel();
    }

    public Authority(@NotNull AuthorityDTO auth, @NotNull String group){
        this.key = new AuthorityPrimaryKey(group, AuthorityType.GROUP, auth.getClient(), auth.getAuthority());
        this.accessLevel = auth.getAccessLevel();
    }

    public Authority(@NotNull AuthorityDTO auth, @NotNull String identifier, AuthorityType authorityType){
        this.key = new AuthorityPrimaryKey(identifier, authorityType, auth.getClient(), auth.getAuthority());
        this.accessLevel = auth.getAccessLevel();
    }

    public String getAuthority(){
        if(this.key == null)
            return  null;
        return key.getAuthority();
    }

    public String getId(){
        if(this.key == null)
            return  null;
        return key.getId();
    }

    @PrimaryKeyClass
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AuthorityPrimaryKey {

        @PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED, ordinal = 0)
        private String id;

        @PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED, ordinal = 1)
        private AuthorityType type;

        @PrimaryKeyColumn(type = PrimaryKeyType.CLUSTERED)
        private String client;

        @PrimaryKeyColumn(type = PrimaryKeyType.CLUSTERED)
        private String authority;
    }
}
