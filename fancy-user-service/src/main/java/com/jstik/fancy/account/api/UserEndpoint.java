package com.jstik.fancy.account.api;

import com.jstik.fancy.account.storage.entity.cassandra.user.User;
import com.jstik.fancy.account.storage.service.UserService;
import reactor.core.publisher.Mono;

public class UserEndpoint {

    private UserService userService;

    public Mono<User> addUserTags(){
        return null;
    }

    public Mono<User> deleteUserTags(){
        return null;
    }

    public Mono<User> addUserClients(){
        return null;
    }

    public Mono<User> deleteUserClients(){
        return null;
    }

    public Mono<User> updateUserInfo(){
        return null;
    }

    public  Mono<User> addUserGroups(){
        return  null;
    }

    public  Mono<User> deleteUserGroups(){
        return  null;
    }
}
