package com.jstik.site.cassandra.repository;

import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.Update;
import com.jstik.site.cassandra.exception.EntityAlreadyExistsException;
import com.jstik.site.cassandra.util.CqlUtil;
import org.springframework.data.cassandra.core.ReactiveCassandraOperations;
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;

import javax.inject.Inject;
import java.text.MessageFormat;

public class CustomReactiveCassandraRepositoryImpl<T, ID> implements CustomReactiveCassandraRepository<T, ID> {

    @Inject
    private ReactiveCassandraOperations operations;

    @Override
    public  Mono<Boolean> insertIfNotExist(T entity) {
        Insert insert = CqlUtil.createIfNotExistsInsert(operations.getConverter(), entity);
        Assert.notNull(entity, "Entity must not be null");
        return operations.getReactiveCqlOperations().execute(insert);
    }

    @Override
    public <S extends T> Mono<S> insertIfNotExistOrThrow(S entity) throws EntityAlreadyExistsException {
        Insert insert = CqlUtil.createIfNotExistsInsert(operations.getConverter(), entity);
        Assert.notNull(entity, "Entity must not be null");
        return operations.getReactiveCqlOperations().execute(insert).doOnNext(it -> {
            if (!it)
                throw new EntityAlreadyExistsException(MessageFormat.format("Entity {0} already exists ", entity.getClass().getCanonicalName()));
        }).map(it -> entity);
    }

    @Override
    public Mono<Boolean> updateIfExist(T entity) {
        Update update = CqlUtil.createIfExistsUpdate(operations.getConverter(), entity);
        Assert.notNull(entity, "Entity must not be null");
        return operations.getReactiveCqlOperations().execute(update);
    }
}
