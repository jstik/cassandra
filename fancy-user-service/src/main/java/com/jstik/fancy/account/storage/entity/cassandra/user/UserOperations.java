package com.jstik.fancy.account.storage.entity.cassandra.user;

import com.jstik.fancy.account.model.user.IUser;
import com.jstik.site.cassandra.model.EntityOperation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Table("user_by_change_date")
@NoArgsConstructor
public class UserOperations {

    @PrimaryKey
    private UserOperationsPrimaryKey primaryKey;

    public UserOperations(IUser user, EntityOperation operation) {
        this.primaryKey = new UserOperationsPrimaryKey(user.getLogin(), operation);
    }

    @Getter
    @Setter
    @PrimaryKeyClass
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserOperationsPrimaryKey {

        @PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED, ordering = Ordering.DESCENDING)
        private LocalDate day = LocalDate.now(Clock.systemUTC());


        @PrimaryKeyColumn(type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
        private LocalDateTime timestamp = LocalDateTime.now(Clock.systemUTC());

        @PrimaryKeyColumn(type = PrimaryKeyType.CLUSTERED)
        private EntityOperation operation;

        @PrimaryKeyColumn(type = PrimaryKeyType.CLUSTERED)
        private String login;


        public UserOperationsPrimaryKey(String login, EntityOperation operation) {
            this.login = login;
            this.operation = operation;
        }
    }
}
