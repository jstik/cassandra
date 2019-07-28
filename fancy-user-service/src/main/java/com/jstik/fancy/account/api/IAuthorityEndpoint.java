package com.jstik.fancy.account.api;

import com.jstik.fancy.account.model.authority.AuthorityDTO;
import com.jstik.fancy.account.storage.entity.cassandra.authority.Authority;
import com.jstik.fancy.account.storage.entity.cassandra.authority.AuthorityType;
import com.jstik.fancy.account.storage.entity.cassandra.user.User;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;
import java.util.Collection;

public interface IAuthorityEndpoint {

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "login not found")})
    @GetMapping(value = "/users/{login}/authority")
    Flux<Authority> getUserAuthorities(@PathVariable(name="login") String login, String client);


    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "login not found"),
            @ApiResponse(code = 405, message = "Validation exception")})
    @PutMapping(value = "/users/{login}/authority")
    Mono<Boolean> addUserAuthorities(@PathVariable(name="login") String login, @NotNull Collection<AuthorityDTO> authorities);

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "login not found"),
            @ApiResponse(code = 405, message = "Validation exception")})
    @DeleteMapping(value = "/users/{login}/authority")
    Mono<Boolean> deleteUserAuthorities(@PathVariable(name="login") String login, String client, @NotNull Collection<String> authorities);


    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok")})
    @GetMapping(value = "/groups/{groupId}/authority")
    Flux<Authority> getGroupAuthorities(@PathVariable(name="group") String groupId, String client);


    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 405, message = "Validation exception")})
    @PutMapping(value = "/groups/{groupId}/authority")
    Mono<Boolean> addGroupAuthorities(@PathVariable(name="group") String groupId, @NotNull Collection<AuthorityDTO> authorities);

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 405, message = "Validation exception")})
    @DeleteMapping(value = "/groups/{groupId}/authority")
    Mono<Boolean> deleteroupAuthorities(@PathVariable(name="group") String groupId, String client, @NotNull Collection<String> authorities);


}
