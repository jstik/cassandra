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

@RequestMapping(value = "/user")
public interface IUserEndpoint {

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "login not found"),
            @ApiResponse(code = 405, message = "Validation exception")})
    @GetMapping(value = "/{login}/{client}")
    Mono<User> getUserDetails( @PathVariable String login, @PathVariable String client);

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "login not found"),
            @ApiResponse(code = 405, message = "Validation exception")})
    @PutMapping(value = "/{login}/tags")
    Mono<User> addUserTags(@RequestParam @NotNull Collection<String> tags, @PathVariable(name="login") String login);

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "login not found"),
            @ApiResponse(code = 405, message = "Validation exception")})
    @DeleteMapping(value = "/{login}/tags")
    Mono<User> deleteUserTags(@RequestParam @NotNull Collection<String> tags, @PathVariable(name="login") String login);

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "login not found"),
            @ApiResponse(code = 405, message = "Validation exception")})
    @PutMapping(value = "/{login}/clients")
    Mono<User> addUserClients(@RequestParam  @NotNull Collection<String> clients, @PathVariable(name="login") String login);

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "login not found"),
            @ApiResponse(code = 405, message = "Validation exception")})
    @DeleteMapping(value = "/{login}/clients")
    Mono<User> deleteUserClients(@RequestParam @NotNull Collection<String> clients, @PathVariable(name="login") String login);

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "login not found"),
            @ApiResponse(code = 405, message = "Validation exception")})
    @PatchMapping(value = "/{login}/userinfo")
    Mono<User> updateUserInfo(@RequestParam @NotNull @Valid UpdateUserRequest request, @PathVariable(name="login") String login);

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "login not found"),
            @ApiResponse(code = 405, message = "Validation exception")})
    @PutMapping(value = "/{login}/groups")
    Mono<User> addUserGroups(@RequestParam @NotNull Collection<String> groups, @PathVariable(name="login") String login);

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "login not found"),
            @ApiResponse(code = 405, message = "Validation exception")})
    @DeleteMapping(value = "/{login}/groups")
    Mono<User> deleteUserGroups(@RequestParam @NotNull Collection<String> groups, @PathVariable(name="login") String login);




}
