package com.jstik.fancy.account.storage.service;

import com.jstik.fancy.account.storage.dao.repository.cassandra.authority.AuthorityRepository;
import com.jstik.fancy.account.storage.entity.cassandra.authority.Authority;
import com.jstik.fancy.account.storage.entity.cassandra.authority.AuthorityType;
import com.jstik.fancy.account.model.authority.AuthorityDTO;
import com.jstik.fancy.account.storage.entity.cassandra.user.User;
import com.jstik.site.cassandra.statements.EntityAwareBatchStatement;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

import static com.jstik.site.cassandra.statements.DMLStatementProducerBuilder.insertProducer;

public class AuthorityService implements IAuthorityService {

    private final AuthorityRepository authorityRepository;

    @Inject
    public AuthorityService(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    @Override
    public Mono<Boolean> addAuthorities(String identifier, Collection<AuthorityDTO> authorities, AuthorityType type) {
        if (authorities == null || authorities.isEmpty())
            return Mono.just(true);
        if (identifier == null)
            return Mono.error(new IllegalStateException("Identifier should be provided!"));
        if (type == null)
            return Mono.error(new IllegalStateException("AuthorityType should be provided!"));
        Collection<Authority> entities = authorities.stream().filter(Objects::nonNull)
                .map(dto -> new Authority(dto, identifier, type))
                .collect(Collectors.toList());
        if (entities.stream().anyMatch(e -> e.getKey().getClient() == null))
            return Mono.error(new IllegalStateException("Client should be provided for all provided authorities!"));
        if (entities.stream().anyMatch(e -> e.getKey().getAuthority() == null))
            return Mono.error(new IllegalStateException("Authority should be provided for all provided authorities!"));
        EntityAwareBatchStatement batchStatement = insertAuthority(entities).orElse(null);
        if (batchStatement == null)
            return Mono.just(true);
        return authorityRepository.executeBatch(batchStatement);
    }

    @Override
    public Mono<Boolean> deleteAuthorities(String client, String identifier, Collection<String> authorities, AuthorityType type) {
        if (authorities == null || authorities.isEmpty())
            return Mono.just(true);
        if (identifier == null)
            return Mono.error(new IllegalStateException("Identifier should be provided!"));
        if (type == null)
            return Mono.error(new IllegalStateException("AuthorityType should be provided!"));
        if (client == null)
            return Mono.error(new IllegalStateException("Client should be provided!"));
        return authorityRepository.deleteAuthoritiesForClient(client, identifier, authorities, type);
    }

    @Override
    public Flux<Authority> findAuthorities(String client, String identifier, AuthorityType type) {
        if (client == null)
            return Flux.error(new IllegalStateException("Client should be provided!"));
        if (identifier == null)
            return Flux.error(new IllegalStateException("Identifier should be provided!"));
        if (type == null)
            return Flux.error(new IllegalStateException("AuthorityType should be provided!"));
        return authorityRepository.findAllByKeyClientAndKeyIdAndKeyType(client, identifier, type);
    }

    @Override
    public Flux<Authority> findAuthorities(String client, Collection<String> ids, AuthorityType type) {
        if (ids == null || ids.isEmpty())
            return Flux.empty();
        if (client == null)
            return Flux.error(new IllegalStateException("Client should be provided!"));
        if (type == null)
            return Flux.error(new IllegalStateException("AuthorityType should be provided!"));
        return authorityRepository.findAllByKeyClientAndKeyTypeAndKeyIdIn(client, type, ids);
    }

    public  Mono<List<Authority>> findUserAuthorities(String client, User user){
        return findAuthorities(client, user.getLogin(), AuthorityType.USER)
                .collectList();
    }

    public Mono<List<Authority>> findGroupsAuthorities(String client, User user){
        if(user.getGroups() == null || user.getGroups().isEmpty())
            return Mono.just(Collections.EMPTY_LIST);
        return  findAuthorities(client, user.getGroups(), AuthorityType.GROUP)
                .collectList();
    }

    @Override
    public Optional<EntityAwareBatchStatement> insertAuthority(Collection<Authority> authorities) {
        if (authorities == null)
            return Optional.empty();
        return authorities.stream()
                .map(authority -> new EntityAwareBatchStatement(insertProducer(), authority))
                .reduce(EntityAwareBatchStatement::andThen);
    }

}
