package com.jstik.fancy.account.service;

import com.jstik.fancy.account.entity.cassandra.user.User;

public class TestUserUtil {

    public static User prepareUser(String login) {
        return new User(login, "firstName", "lastName", "email@email.com");
    }
}
