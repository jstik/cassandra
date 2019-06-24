package com.jstik.fancy.account.dao.repository;

import com.jstik.fancy.account.entity.user.User;
import com.jstik.fancy.account.entity.authority.Authority;
import com.jstik.fancy.account.entity.user.UserOperations;
import com.jstik.site.cassandra.model.EntityOperation;
import com.jstik.site.cassandra.repository.CustomReactiveCassandraRepository;
import com.jstik.site.cassandra.statements.EntityAwareBatchStatement;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Optional;

import static com.jstik.site.cassandra.statements.DMLStatementProducerBuilder.insertProducer;

public interface UserRepository extends ReactiveCassandraRepository<User, User.UserPrimaryKey>, CustomReactiveCassandraRepository<User, User.UserPrimaryKey> {


    Mono<User> findByPrimaryKeyLogin(String login);


    default Optional<EntityAwareBatchStatement> userAuthority(Collection<Authority> authorities) {
        if (authorities == null)
            return Optional.empty();
        return authorities.stream()
                .map(authority -> new EntityAwareBatchStatement(insertProducer(), authority))
                .reduce(EntityAwareBatchStatement::andThen);
    }


    default Optional<EntityAwareBatchStatement> userOperationStatement(@NotNull User user, EntityOperation entityOperation) {
        UserOperations operation = new UserOperations(user, entityOperation);
        return Optional.of(new EntityAwareBatchStatement(insertProducer(), operation));
    }
}
