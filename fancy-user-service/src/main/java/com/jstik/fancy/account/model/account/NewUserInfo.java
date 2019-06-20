package com.jstik.fancy.account.model.account;

import com.jstik.fancy.account.entity.user.User;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

@Getter
@Setter
public class NewUserInfo {

    private User user;

    private String regKey;

    private Collection<String> rejectedClients;


}
