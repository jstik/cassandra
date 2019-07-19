package com.jstik.fancy.account.storage.service;

import com.jstik.fancy.account.model.user.NewUserInfo;
import com.jstik.fancy.account.storage.entity.cassandra.user.User;
import com.jstik.fancy.account.storage.entity.cassandra.user.UserRegistration;
import reactor.core.publisher.Mono;

import java.util.Collection;

public interface IUserService {
    Mono<NewUserInfo> createUser(User user, Mono<UserRegistration> registration);

    Mono<User> addUserTags(Collection<String> tags, User user);

    Mono<User> deleteUserTags(Collection<String> tags, User user);

    Mono<User> addUserClients(Collection<String> clients, User user);

    Mono<User> deleteUserClients(Collection<String> clients, User user);

    Mono<User> updateUser(User user);

    Mono<User> addUserGroups(Collection<String> groups, User user);

    Mono<User> deleteUserGroups(Collection<String> groups, User user);

    Mono<User> activateUser(User user, String password);

    Mono<User> findUserOrThrow(String login);
}
