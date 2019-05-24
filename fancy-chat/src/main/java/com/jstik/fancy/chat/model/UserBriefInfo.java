package com.jstik.fancy.chat.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserBriefInfo {

    private String login;
    private String name;
    private String photoLink;
    private LocalDateTime timeStamp;

}
