package com.jstik.fancy.chat.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Table("rooms")
@Getter
@Setter
public class Room {

    @PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED, name = "room_name")
    private String roomName;

    private String banner;
    private LocalDateTime created;
    private String creator;
    private Set<UserBriefInfo> participants = new HashSet<>();
}
