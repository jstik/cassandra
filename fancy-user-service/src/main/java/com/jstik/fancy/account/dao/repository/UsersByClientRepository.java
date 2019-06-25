package com.jstik.fancy.account.dao.repository;

import com.jstik.fancy.account.entity.cassandra.client.UsersByClient;
import com.jstik.site.cassandra.repository.CustomReactiveCassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Mono;

import java.util.Collection;

public interface UsersByClientRepository extends ReactiveCassandraRepository<UsersByClient, String>, CustomReactiveCassandraRepository<UsersByClient, String> {

    @Query(" delete from users_by_client where  login= :login and client in :clients;")
    Mono<Boolean> deleteUsersByClient(@Param("login") String login, @Param("clients") Collection<String> clients);
}
