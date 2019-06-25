package com.jstik.fancy.account.model.user;

import com.jstik.fancy.account.entity.cassandra.user.User;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

@Getter
@Setter
public class NewUserInfo {

    private User user;

    private String regKey;

    private Collection<String> rejectedClients;

    private boolean linkedInserted;


}
