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
@Table("user_authority")
@NoArgsConstructor
@AllArgsConstructor
public class Authority {

    @PrimaryKey
    private UserAuthorityPrimaryKey userAuthorityPrimaryKey;

    @Column("access_level")
    private AccessLevel accessLevel;

    public Authority(@NotNull AuthorityDTO auth, @NotNull User user){
        this.userAuthorityPrimaryKey = new UserAuthorityPrimaryKey(user.getLogin(), AuthorityType.USER, auth.getClient(), auth.getAuthority());
        this.accessLevel = auth.getAccessLevel();
    }

    @PrimaryKeyClass
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserAuthorityPrimaryKey{

        @PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED, ordinal = 0)
        private String identifier;

        @PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED, ordinal = 1)
        private AuthorityType authorityType;

        @PrimaryKeyColumn(type = PrimaryKeyType.CLUSTERED)
        private String client;

        @PrimaryKeyColumn(type = PrimaryKeyType.CLUSTERED)
        private String authority;
    }
}
