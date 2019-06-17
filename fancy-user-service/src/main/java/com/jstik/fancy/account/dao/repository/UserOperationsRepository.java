package com.jstik.fancy.account.dao.repository;

import com.jstik.fancy.account.entity.user.UserOperations;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

public interface UserOperationsRepository extends ReactiveCassandraRepository<UserOperations, UserOperations.UserOperationsPrimaryKey> {


    Flux<UserOperations> findAllByPrimaryKeyDay(LocalDate date);
}
