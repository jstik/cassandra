package com.jstik.site.cassandra.repository;

import com.datastax.driver.core.RegularStatement;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.querybuilder.Batch;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.jstik.site.cassandra.exception.EntityAlreadyExistsException;
import com.jstik.site.cassandra.statements.DMLStatementProducerBuilder;
import com.jstik.site.cassandra.statements.EntityAwareBatchStatement;
import org.springframework.data.cassandra.core.ReactiveCassandraOperations;
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;

import javax.inject.Inject;
import java.text.MessageFormat;

import static java.text.MessageFormat.format;

public class CustomReactiveCassandraRepositoryImpl<T, ID> implements CustomReactiveCassandraRepository<T, ID> {

    private final ReactiveCassandraOperations operations;

    @Inject
    public CustomReactiveCassandraRepositoryImpl(ReactiveCassandraOperations operations) {
        this.operations = operations;
    }

    @Override
    public Mono<Boolean> insertIfNotExist(T entity) {
        Assert.notNull(entity, "Entity must not be null");
        RegularStatement insert = DMLStatementProducerBuilder.insertIfNotExistsProducer().apply(operations.getConverter(), entity);
        return operations.getReactiveCqlOperations().execute(insert);
    }

    @Override
    public <S extends T> Mono<S> insertIfNotExistOrThrow(S entity) throws EntityAlreadyExistsException {
        Assert.notNull(entity, "Entity must not be null");
        RegularStatement insert = DMLStatementProducerBuilder.insertIfNotExistsProducer().apply(operations.getConverter(), entity);
        return insertOrThrow(insert, entity);
    }


    @Override
    public Mono<Boolean> updateIfExist(T entity) {
        Assert.notNull(entity, "Entity must not be null");
        RegularStatement update = DMLStatementProducerBuilder.updateByIdProducer(true).apply(operations.getConverter(), entity);
        return operations.getReactiveCqlOperations().execute(update);
    }

    @Override
    public Mono<Boolean> executeBatch(EntityAwareBatchStatement batchStatement) {
        Assert.notNull(batchStatement, "Batch must not be null");
        Batch batch = QueryBuilder.batch();
        batchStatement.accept(operations.getConverter(), batch);
        return operations.getReactiveCqlOperations().execute(batch);
    }

    private <S extends T> Mono<S> insertOrThrow(Statement statement, S entity) throws EntityAlreadyExistsException {
        return operations.getReactiveCqlOperations().execute(statement).doOnNext(it -> {
            if (!it)
                throw new EntityAlreadyExistsException(format("Entity {0} already exists ", entity.getClass().getCanonicalName()));
        }).map(it -> entity);
    }

}
