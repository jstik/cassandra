package com.jstik.fancy.account.storage.dao.repository.cassandra.user;

import com.jstik.fancy.account.handler.operation.DMLOperationHandler;
import com.jstik.fancy.account.model.user.IUser;
import com.jstik.fancy.account.storage.entity.cassandra.user.User;
import com.jstik.fancy.account.storage.entity.cassandra.user.UserOperations;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

import static com.jstik.site.cassandra.model.EntityOperation.CREATE;
import static com.jstik.site.cassandra.model.EntityOperation.UPDATE;

public interface UserOperationsRepository extends ReactiveCrudRepository<UserOperations, UserOperations.UserOperationsPrimaryKey>, DMLOperationHandler<IUser> {


    Flux<UserOperations> findAllByPrimaryKeyDay(LocalDate date);

    @Override
    default <S extends IUser> Mono<S> handleInsert(Mono<S> userMono){
        return userMono.doOnSuccess(user -> {
            UserOperations operation = new UserOperations(user, CREATE);
            save(operation).subscribe();
        });
    }

    @Override
    default <S extends IUser> Mono<S> handleUpdate(Mono<S> userMono){
        return userMono.doOnSuccess(user -> {
            UserOperations operation = new UserOperations(user, UPDATE);
            save(operation).subscribe();
        });
    }
}
