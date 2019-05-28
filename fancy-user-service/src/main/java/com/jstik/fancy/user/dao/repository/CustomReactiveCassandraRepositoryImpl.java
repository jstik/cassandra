package com.jstik.fancy.user.dao.repository;

import com.datastax.driver.core.querybuilder.Insert;
import com.jstik.site.cassandra.util.CqlUtil;
import org.springframework.data.cassandra.core.ReactiveCassandraOperations;
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;

import javax.inject.Inject;

public class CustomReactiveCassandraRepositoryImpl<T, ID> implements CustomReactiveCassandraRepository<T, ID> {

    @Inject
    private ReactiveCassandraOperations operations;

    @Override
    public <S extends T> Mono<S> insertIfNotExist(S entity) {
        Insert insert = CqlUtil.createIfNotExistsInsert(operations.getConverter(), entity);
        Assert.notNull(entity, "Entity must not be null");
        return operations.getReactiveCqlOperations().execute(insert).map(it -> entity);
    }
}
