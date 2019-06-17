package com.jstik.fancy.account.dao.repository;

import com.jstik.fancy.account.entity.user.User;
import com.jstik.site.cassandra.repository.CustomReactiveCassandraRepository;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCassandraRepository<User, User.UserPrimaryKey>, CustomReactiveCassandraRepository<User, User.UserPrimaryKey> {


    Mono<User> findByPrimaryKeyLogin(String login);
}
