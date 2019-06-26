package com.jstik.fancy.account.storage.dao.repository.cassandra.user;

import com.jstik.fancy.account.storage.entity.cassandra.user.User;
import com.jstik.site.cassandra.repository.CustomReactiveCassandraRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<User, User.UserPrimaryKey>, CustomReactiveCassandraRepository<User, User.UserPrimaryKey> {


    Mono<User> findByPrimaryKeyLogin(String login);

}
