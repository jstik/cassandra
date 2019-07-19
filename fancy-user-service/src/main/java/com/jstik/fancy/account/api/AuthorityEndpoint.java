package com.jstik.fancy.account.api;

import com.jstik.fancy.account.model.authority.AuthorityDTO;
import com.jstik.fancy.account.storage.entity.cassandra.authority.Authority;
import com.jstik.fancy.account.storage.entity.cassandra.authority.AuthorityType;
import com.jstik.fancy.account.storage.service.IAuthorityService;
import com.jstik.fancy.account.storage.service.IUserService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.util.Collection;

public class AuthorityEndpoint implements IAuthorityEndpoint {

    private final IAuthorityService authorityService;

    public AuthorityEndpoint(IAuthorityService authorityService) {
        this.authorityService = authorityService;
    }


    @Override
    public Flux<Authority> getUserAuthorities(String login, String client) {
        return authorityService.findAuthorities( client, login, AuthorityType.USER);
    }

    @Override
    public Mono<Boolean> addUserAuthorities(String login, @NotNull Collection<AuthorityDTO> authorities) {
        return authorityService.addAuthorities(login, authorities, AuthorityType.USER);

    }

    @Override
    public Mono<Boolean> deleteUserAuthorities(String login, String client, @NotNull Collection<String> authorities) {
        return authorityService.deleteAuthorities(login, client, authorities, AuthorityType.USER);
    }

    @Override
    public Flux<Authority> getGroupAuthorities(String groupId, String client) {
        return authorityService.findAuthorities( client, groupId, AuthorityType.GROUP);
    }

    @Override
    public Mono<Boolean> addGroupAuthorities(String groupId, @NotNull Collection<AuthorityDTO> authorities) {
        return authorityService.addAuthorities(groupId, authorities, AuthorityType.GROUP);
    }

    @Override
    public Mono<Boolean> deleteroupAuthorities(String groupId, String client, @NotNull Collection<String> authorities) {
        return authorityService.deleteAuthorities(groupId, client, authorities, AuthorityType.GROUP);
    }


}
