package com.jstik.fancy.account.storage.dao.repository.cassandra.user;

import com.jstik.fancy.account.storage.entity.cassandra.user.UserOperations;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

public interface UserOperationsRepository extends ReactiveCrudRepository<UserOperations, UserOperations.UserOperationsPrimaryKey> {


    Flux<UserOperations> findAllByPrimaryKeyDay(LocalDate date);
}
