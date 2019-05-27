package com.jstik.fancy.chat.model.entity;


import com.datastax.driver.core.utils.UUIDs;
import lombok.*;
import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.*;

import java.time.LocalDate;
import java.util.UUID;


@Table
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
public class Message {
    @NonNull
    @PrimaryKey
    private MessagePrimaryKey messageKey;

    @Column
    @NonNull
    private String content;

    @Column
    private boolean system;

    @Column
    @NonNull
    private UserBriefInfo creatorInfo;

    @Column
    @NonNull
    private String creator;

    public Message(MessagePrimaryKey messageKey, String content, UserBriefInfo creatorInfo) {
        this.messageKey = messageKey;
        this.content = content;
        this.creatorInfo = creatorInfo;
        this.setCreator(this.creatorInfo.getLogin());
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @RequiredArgsConstructor
    @PrimaryKeyClass
    public static class MessagePrimaryKey {

        @PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED, ordinal = 0)
        @NonNull
        private String address;

        @PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED, ordinal = 1)
        private LocalDate dayCreated = LocalDate.now();

        @PrimaryKeyColumn(type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
        private UUID messageId = UUIDs.timeBased();
    }
}
