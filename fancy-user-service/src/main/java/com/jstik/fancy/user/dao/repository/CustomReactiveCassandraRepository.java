package com.jstik.fancy.user.dao.repository;

import reactor.core.publisher.Mono;

public interface CustomReactiveCassandraRepository  <T, ID> {

    <S extends T> Mono<S> insertIfNotExist(S entity);
}
