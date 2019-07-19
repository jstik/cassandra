package com.jstik.fancy.account.api;

import com.jstik.fancy.account.api.model.UpdateUserRequest;
import com.jstik.fancy.account.storage.entity.cassandra.user.User;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@RestController
public interface IUserEndpoint {

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "login not found"),
            @ApiResponse(code = 405, message = "Validation exception")})
    @PutMapping(value = "/{login}/tags")
    Mono<User> addUserTags(@NotNull Collection<String> tags, @PathVariable(name="login") String login);

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "login not found"),
            @ApiResponse(code = 405, message = "Validation exception")})
    @DeleteMapping(value = "/{login}/tags")
    Mono<User> deleteUserTags(@NotNull Collection<String> tags, @PathVariable(name="login") String login);

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "login not found"),
            @ApiResponse(code = 405, message = "Validation exception")})
    @PutMapping(value = "/{login}/clients")
    Mono<User> addUserClients(@NotNull Collection<String> clients, @PathVariable(name="login") String login);

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "login not found"),
            @ApiResponse(code = 405, message = "Validation exception")})
    @DeleteMapping(value = "/{login}/clients")
    Mono<User> deleteUserClients(@NotNull Collection<String> clients, @PathVariable(name="login") String login);

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "login not found"),
            @ApiResponse(code = 405, message = "Validation exception")})
    @PatchMapping(value = "/{login}/userinfo")
    Mono<User> updateUserInfo(@NotNull @Valid UpdateUserRequest request, @PathVariable(name="login") String login);

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "login not found"),
            @ApiResponse(code = 405, message = "Validation exception")})
    @PutMapping(value = "/{login}/groups")
    Mono<User> addUserGroups(@NotNull Collection<String> groups, @PathVariable(name="login") String login);

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "login not found"),
            @ApiResponse(code = 405, message = "Validation exception")})
    @DeleteMapping(value = "/{login}/groups")
    Mono<User> deleteUserGroups(@NotNull Collection<String> groups, @PathVariable(name="login") String login);
}
