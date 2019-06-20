package com.jstik.fancy.account.entity.user;

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
public class UserAuthority {

    @PrimaryKey
    private UserAuthorityPrimaryKey userAuthorityPrimaryKey;

    @Column("access_level")
    private AccessLevel accessLevel;

    public UserAuthority(@NotNull AuthorityDTO auth, @NotNull User user){
        this.userAuthorityPrimaryKey = new UserAuthorityPrimaryKey(user.getLogin(), auth.getClient(), auth.getAuthority());
        this.accessLevel = auth.getAccessLevel();
    }

    @PrimaryKeyClass
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserAuthorityPrimaryKey{

        @PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED)
        private String login;

        @PrimaryKeyColumn(type = PrimaryKeyType.CLUSTERED)
        private String client;

        @PrimaryKeyColumn(type = PrimaryKeyType.CLUSTERED)
        private String authority;
    }
}
