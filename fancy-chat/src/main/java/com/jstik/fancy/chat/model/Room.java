package com.jstik.fancy.chat.model;

import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDateTime;

@Table("rooms")
public class Room {
    private String roomName;
    private String banner;
    private LocalDateTime created;
    private String creator;



    /*participants set<text>,*/
}
