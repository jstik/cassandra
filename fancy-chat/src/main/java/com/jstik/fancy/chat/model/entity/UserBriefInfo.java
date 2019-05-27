package com.jstik.fancy.chat.model.entity;

import lombok.*;
import org.springframework.data.cassandra.core.mapping.UserDefinedType;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@UserDefinedType("user_brief_info")
public class UserBriefInfo {

    @NonNull
    private String login;
    @NonNull
    private String name;

    private LocalDateTime timeStamp = LocalDateTime.now();
}
