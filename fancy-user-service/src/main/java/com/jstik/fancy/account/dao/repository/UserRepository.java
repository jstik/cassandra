package com.jstik.fancy.account.dao.repository;

import com.jstik.fancy.account.entity.client.UsersByClient;
import com.jstik.fancy.account.entity.tag.UserByTag;
import com.jstik.fancy.account.entity.user.User;
import com.jstik.fancy.account.entity.user.UserAuthority;
import com.jstik.fancy.account.entity.user.UserOperations;
import com.jstik.fancy.account.model.AuthorityDTO;
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

    default Optional<EntityAwareBatchStatement> usersByClientStatement(@NotNull User user, Collection<String> clients) {
        if(clients == null)
            return Optional.empty();
        return clients.stream()
                .map(client -> new EntityAwareBatchStatement(insertProducer(), new UsersByClient(client, user.getLogin())))
                .reduce(EntityAwareBatchStatement::andThen);
    }

    default Optional<EntityAwareBatchStatement> userAuthority(Collection<UserAuthority> authorities) {
        if(authorities == null)
            return Optional.empty();
        return authorities.stream()
                .map(authority -> new EntityAwareBatchStatement(insertProducer(), authority))
                .reduce(EntityAwareBatchStatement::andThen);
    }

    default Optional<EntityAwareBatchStatement> userByTagStatement(@NotNull User user, Collection<String> tags) {
        if(tags == null)
            return Optional.empty();
        return tags.stream()
                .map(tag -> new EntityAwareBatchStatement(insertProducer(), new UserByTag(tag, user.getLogin())))
                .reduce(EntityAwareBatchStatement::andThen);
    }

    default Optional<EntityAwareBatchStatement> userOperationStatement(@NotNull User user, EntityOperation entityOperation) {
        UserOperations operation = new UserOperations(user, entityOperation);
        return Optional.of(new EntityAwareBatchStatement(insertProducer(), operation));
    }
}
