package com.jstik.fancy.account.dao.repository;

import com.jstik.fancy.account.entity.cassandra.authority.Authority;
import com.jstik.fancy.account.entity.cassandra.authority.AuthorityType;
import com.jstik.site.cassandra.repository.CustomReactiveCassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;

public interface AuthorityRepository extends ReactiveCassandraRepository<Authority, Authority.AuthorityPrimaryKey>, CustomReactiveCassandraRepository<Authority, Authority.AuthorityPrimaryKey> {


    @Query("delete from authority where id = :id and client =:client and type =:type and authority in :authorities")
    Mono<Boolean> deleteAuthoritiesForClient(@Param("client") String client,
                                             @Param("id") String identifier,
                                             @Param("authorities") Collection<String> authorities,
                                             @Param("type") AuthorityType type
    );


    Flux<Authority> findAllByKeyClientAndKeyIdAndKeyType(
            @Param("client") String client,
            @Param("id") String id,
            @Param("type") AuthorityType type
    );

    Flux<Authority> findAllByKeyClientAndKeyTypeAndKeyIdIn(
            String client,
            AuthorityType type,
            Collection<String> ids
    );
}
