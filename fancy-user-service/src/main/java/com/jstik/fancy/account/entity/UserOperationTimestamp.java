package com.jstik.fancy.account.entity;

import com.jstik.site.cassandra.model.EntityOperation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.Clock;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table("user_operation_timestamp")
public class UserOperationTimestamp {

    @PrimaryKey
    private UserOperationTimestampPrimaryKey primaryKey;

    public UserOperationTimestamp(String login, EntityOperation operation) {
        this.primaryKey = new UserOperationTimestampPrimaryKey(login, operation);
    }

    @PrimaryKeyClass
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class UserOperationTimestampPrimaryKey {

        @PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED)
        private EntityOperation operation;
        @PrimaryKeyColumn(type = PrimaryKeyType.CLUSTERED, ordinal = 0)
        private LocalDateTime timestamp = LocalDateTime.now(Clock.systemUTC());
        @PrimaryKeyColumn(type = PrimaryKeyType.CLUSTERED, ordinal = 1)
        private String login;

        UserOperationTimestampPrimaryKey(String login, EntityOperation operation) {
            this.login = login;
            this.operation = operation;
        }

    }
}
