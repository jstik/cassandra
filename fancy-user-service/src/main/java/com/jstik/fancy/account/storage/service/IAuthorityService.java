package com.jstik.fancy.account.storage.service;

import com.jstik.fancy.account.model.authority.AuthorityDTO;
import com.jstik.fancy.account.storage.entity.cassandra.authority.Authority;
import com.jstik.fancy.account.storage.entity.cassandra.authority.AuthorityType;
import com.jstik.fancy.account.storage.entity.cassandra.user.User;
import com.jstik.site.cassandra.statements.EntityAwareBatchStatement;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface IAuthorityService {
    Mono<Boolean> addAuthorities(String identifier, Collection<AuthorityDTO> authorities, AuthorityType type);

    Mono<Boolean> deleteAuthorities(String client, String identifier, Collection<String> authorities, AuthorityType type);

    Flux<Authority> findAuthorities(String client, String identifier, AuthorityType type);

    Flux<Authority> findAuthorities(String client, Collection<String> ids, AuthorityType type);

    public  Mono<List<Authority>> findUserAuthorities(String client, User user);

    public Mono<List<Authority>> findGroupsAuthorities(String client, User user);

    Optional<EntityAwareBatchStatement> insertAuthority(Collection<Authority> authorities);
}
