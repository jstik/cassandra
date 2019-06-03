package com.jstik.fancy.user.dao.repository;

import com.jstik.fancy.user.entity.User;
import com.jstik.site.cassandra.repository.CustomReactiveCassandraRepository;
import com.jstik.site.cassandra.statements.EntityAwareBatchStatement;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCassandraRepository<User, User.UserPrimaryKey>, CustomReactiveCassandraRepository<User, User.UserPrimaryKey> {


    Mono<User> findByPrimaryKeyLogin(String login);
}
