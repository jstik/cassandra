package com.jstik.site.cassandra.statements;

import com.datastax.driver.core.RegularStatement;
import com.datastax.driver.core.querybuilder.Batch;
import org.springframework.data.cassandra.core.convert.CassandraConverter;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class EntityAwareBatchStatement implements BiConsumer<CassandraConverter, Batch> {

    private BiFunction<CassandraConverter, Object, RegularStatement> statementFunction;

    private Object entity;

    public EntityAwareBatchStatement(BiFunction<CassandraConverter, Object, RegularStatement> statementFunction, Object entity) {
        this.statementFunction = statementFunction;
        this.entity = entity;
    }

    @Override
    public void accept(CassandraConverter converter, Batch batch) {
        batch.add(statementFunction.apply(converter, entity));
    }


    public EntityAwareBatchStatement andThen(EntityAwareBatchStatement after) {
        return new EntityAwareBatchStatement(statementFunction, entity) {
            public void accept(CassandraConverter converter, Batch batch) {
                EntityAwareBatchStatement.this.accept(converter, batch);
                after.accept(converter, batch);
            }
        };
    }
}
