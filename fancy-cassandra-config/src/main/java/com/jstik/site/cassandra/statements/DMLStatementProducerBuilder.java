package com.jstik.site.cassandra.statements;

import com.datastax.driver.core.RegularStatement;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Update;
import org.springframework.data.cassandra.core.convert.CassandraConverter;
import org.springframework.data.cassandra.core.mapping.CassandraPersistentEntity;

import java.util.function.BiFunction;

public class DMLStatementProducerBuilder {

    public static BiFunction<CassandraConverter, Object, RegularStatement> insertStatementProducer(boolean ifNotExists) {
        return (converter, entity) -> {
            CassandraPersistentEntity<?> persistentEntity = converter.getMappingContext().getRequiredPersistentEntity(entity.getClass());
            Insert insert = QueryBuilder.insertInto(persistentEntity.getTableName().toCql());
            converter.write(entity, insert, persistentEntity);
            if (ifNotExists)
                insert.ifNotExists();
            return insert;
        };
    }

    public static BiFunction<CassandraConverter, Object, RegularStatement> updateByIdStatementProducer(boolean ifExists) {
        return (converter, entity) -> {
            CassandraPersistentEntity<?> persistentEntity = converter.getMappingContext().getRequiredPersistentEntity(entity.getClass());
            Update update = QueryBuilder.update(persistentEntity.getTableName().toCql());
            converter.write(entity, update, persistentEntity);
            if (ifExists)
                update.where().ifExists();
            return update;
        };
    }
}
