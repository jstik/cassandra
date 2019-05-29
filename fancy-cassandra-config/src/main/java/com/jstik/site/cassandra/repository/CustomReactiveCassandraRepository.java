package com.jstik.site.cassandra.repository;


import com.datastax.driver.core.querybuilder.Batch;
import com.jstik.site.cassandra.exception.EntityAlreadyExistsException;
import com.jstik.site.cassandra.statements.EntityAwareBatchStatement;
import reactor.core.publisher.Mono;
public interface CustomReactiveCassandraRepository<T, ID> {

    Mono<Boolean> insertIfNotExist(T entity);

    <S extends T> Mono<S> insertIfNotExistOrThrow(S entity) throws EntityAlreadyExistsException;


    Mono<Boolean> updateIfExist(T entity);

    Mono<Boolean> executeBatch(EntityAwareBatchStatement batch);
}
