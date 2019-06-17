package com.jstik.fancy.account.entity.user;

import com.jstik.fancy.account.model.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.*;

@Getter
@Setter
@Table("user_authority")
public class UserAuthority {

    @PrimaryKey
    private UserAuthorityPrimaryKey userAuthorityPrimaryKey;

    @Column("access_level")
    private AccessLevel accessLevel;

    @PrimaryKeyClass
    @Getter
    @Setter
    public static class UserAuthorityPrimaryKey{

        @PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED)
        private String login;

        @PrimaryKeyColumn(type = PrimaryKeyType.CLUSTERED)
        private String client;

        @PrimaryKeyColumn(type = PrimaryKeyType.CLUSTERED)
        private String authority;
    }
}
