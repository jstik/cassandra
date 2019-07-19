package com.jstik.fancy.account.api;

import com.jstik.fancy.account.api.model.UpdateUserRequest;
import com.jstik.fancy.account.storage.entity.cassandra.user.User;
import com.jstik.fancy.account.storage.service.IUserService;
import reactor.core.publisher.Mono;

import javax.inject.Inject;
import java.util.Collection;

public class UserEndpoint implements IUserEndpoint {

    private final IUserService userService;

    @Inject
    public UserEndpoint(IUserService userService) {
        this.userService = userService;
    }

    @Override
    public Mono<User> addUserTags(Collection<String> tags, String login) {
        return userService.findUserOrThrow(login).flatMap(user -> userService.addUserTags(tags, user));
    }

    @Override
    public Mono<User> deleteUserTags(Collection<String> tags, String login) {
        return userService.findUserOrThrow(login).flatMap(user -> userService.deleteUserTags(tags, user));
    }

    @Override
    public Mono<User> addUserClients(Collection<String> clients, String login) {
        return userService.findUserOrThrow(login).flatMap(user -> userService.addUserClients(clients, user));
    }

    @Override
    public Mono<User> deleteUserClients(Collection<String> clients, String login) {
        return userService.findUserOrThrow(login).flatMap(user -> userService.deleteUserClients(clients, user));
    }

    @Override
    public Mono<User> updateUserInfo(UpdateUserRequest request, String login) {
        return userService
                .findUserOrThrow(login)
                .flatMap(user -> {
                    if (request.getFirstName() != null) {
                        user.setFirstName(request.getFirstName());
                    }
                    if (request.getLastName() != null) {
                        user.setLastName(request.getLastName());
                    }
                    if (request.getEmail() != null) {
                        user.setEmail(request.getEmail());
                    }
                   return userService.updateUser(user);
                });
    }

    @Override
    public Mono<User> addUserGroups(Collection<String> groups, String login) {
        return userService.findUserOrThrow(login).flatMap(user -> userService.addUserGroups(groups, user));
    }

    @Override
    public Mono<User> deleteUserGroups(Collection<String> groups, String login) {
        return userService.findUserOrThrow(login).flatMap(user -> userService.deleteUserGroups(groups, user));
    }
}
