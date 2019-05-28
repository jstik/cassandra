package com.jstik.site.cassandra.repository;

import com.jstik.site.cassandra.exception.EntityAlreadyExistsException;
import reactor.core.publisher.Mono;

public interface CustomReactiveCassandraRepository  <T, ID> {

    Mono<Boolean> insertIfNotExist(T entity);

    <S extends T> Mono<S> insertIfNotExistOrThrow(S entity) throws EntityAlreadyExistsException;


    Mono<Boolean> updateIfExist(T entity);


}
