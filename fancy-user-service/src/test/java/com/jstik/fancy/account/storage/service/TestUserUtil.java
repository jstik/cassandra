package com.jstik.fancy.account.storage.service;

import com.jstik.fancy.account.storage.entity.cassandra.user.User;

public class TestUserUtil {

    public static User prepareUser(String login) {
        return new User(login, "firstName", "lastName", "email@email.com");
    }
}
