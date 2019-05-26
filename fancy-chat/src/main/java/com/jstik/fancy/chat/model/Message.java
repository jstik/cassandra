package com.jstik.fancy.chat.model;


import lombok.*;
import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.*;

import java.util.UUID;


@Table
@Getter
@Setter
@RequiredArgsConstructor
public class Message {
    @NonNull
    @PrimaryKey
    private  MessagePK messagePK;

    @Column
    private String content;

    private boolean system;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @PrimaryKeyClass
    public static class MessagePK{

        @PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED)
        private String address;

        @PrimaryKeyColumn(type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
        private UUID messageId;
    }
}
