package com.jstik.fancy.chat.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Table("rooms")
@Getter
@Setter
public class Room {

    @PrimaryKey(value = "room_name")
    private String roomName;

    private String banner;
    private LocalDateTime created;
    private String creator;
    private Set<UserBriefInfo> participants = new HashSet<>();
}
