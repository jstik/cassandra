package com.jstik.fancy.account.dao.repository.cassandra.user;

import com.jstik.fancy.account.entity.cassandra.user.User;
import com.jstik.fancy.account.entity.cassandra.authority.Authority;
import com.jstik.fancy.account.entity.cassandra.user.UserOperations;
import com.jstik.site.cassandra.model.EntityOperation;
import com.jstik.site.cassandra.repository.CustomReactiveCassandraRepository;
import com.jstik.site.cassandra.statements.EntityAwareBatchStatement;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Optional;

import static com.jstik.site.cassandra.statements.DMLStatementProducerBuilder.insertProducer;

public interface UserRepository extends ReactiveCrudRepository<User, User.UserPrimaryKey>, CustomReactiveCassandraRepository<User, User.UserPrimaryKey> {


    Mono<User> findByPrimaryKeyLogin(String login);

}
