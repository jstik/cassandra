package com.jstik.fancy.account.storage.dao.repository.cassandra.user;

import com.jstik.fancy.account.handler.operation.DMLOperationHandler;
import com.jstik.fancy.account.storage.entity.cassandra.user.User;
import com.jstik.fancy.account.storage.entity.cassandra.user.UserOperations;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

import static com.jstik.site.cassandra.model.EntityOperation.CREATE;
import static com.jstik.site.cassandra.model.EntityOperation.UPDATE;

public interface UserOperationsRepository extends ReactiveCrudRepository<UserOperations, UserOperations.UserOperationsPrimaryKey>, DMLOperationHandler<User> {


    Flux<UserOperations> findAllByPrimaryKeyDay(LocalDate date);

    default Mono<User> handleInsert(Mono<User> userMono) {
        return userMono.doOnSuccess(user -> {
            UserOperations operation = new UserOperations(user, CREATE);
            save(operation).subscribe();
        });
    }

    default Mono<User> handleUpdate(Mono<User> userMono) {
        return userMono.doOnSuccess(user -> {
            UserOperations operation = new UserOperations(user, UPDATE);
            save(operation).subscribe();
        });
    }


}
