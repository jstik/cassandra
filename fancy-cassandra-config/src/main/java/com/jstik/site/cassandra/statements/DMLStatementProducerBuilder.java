package com.jstik.site.cassandra.statements;

import com.datastax.driver.core.RegularStatement;
import com.datastax.driver.core.querybuilder.*;
import org.apache.catalina.User;
import org.springframework.data.cassandra.core.convert.CassandraConverter;
import org.springframework.data.cassandra.core.cql.QueryOptionsUtil;
import org.springframework.data.cassandra.core.cql.WriteOptions;
import org.springframework.data.cassandra.core.mapping.CassandraMappingContext;
import org.springframework.data.cassandra.core.mapping.CassandraPersistentEntity;

import java.util.function.BiFunction;

public class DMLStatementProducerBuilder {

    public static BiFunction<CassandraConverter, Object, RegularStatement> insertProducer() {
        return insertProducer(false, null);
    }

    public static BiFunction<CassandraConverter, Object, RegularStatement> insertIfNotExistsProducer() {
        return insertProducer(true, null);
    }

    public static BiFunction<CassandraConverter, Object, RegularStatement> insertWithTTLProducer(int ttl) {
        return insertProducer(false, ttl);
    }

    private static BiFunction<CassandraConverter, Object, RegularStatement> insertProducer(boolean ifNotExists, Integer ttl) {
        return (converter, entity) -> {
            CassandraPersistentEntity<?> persistentEntity = converter.getMappingContext().getRequiredPersistentEntity(entity.getClass());
            Insert insert = QueryBuilder.insertInto(persistentEntity.getTableName().toCql());
            converter.write(entity, insert, persistentEntity);
            if (ifNotExists)
                insert.ifNotExists();
            if (ttl != null)
                insert.using(QueryBuilder.ttl(ttl));
            return insert;
        };
    }

    public static BiFunction<CassandraConverter, Object, RegularStatement> updateByIdProducer(boolean ifExists) {
        return (converter, entity) -> {
            CassandraPersistentEntity<?> persistentEntity = converter.getMappingContext().getRequiredPersistentEntity(entity.getClass());
            Update update = QueryBuilder.update(persistentEntity.getTableName().toCql());
            converter.write(entity, update, persistentEntity);
            if (ifExists)
                update.where().ifExists();
            return update;
        };
    }

    public static BiFunction<CassandraConverter, Object, RegularStatement>  deleteProducer(boolean ifExists){
        return (converter, entity) -> {
            CassandraPersistentEntity<?> persistentEntity = converter.getMappingContext().getRequiredPersistentEntity(entity.getClass());
            Delete.Selection deleteSelection = QueryBuilder.delete();
            Delete delete = deleteSelection.from(persistentEntity.getTableName().toCql());
            converter.write(entity, delete, persistentEntity);
            if (ifExists)
                delete.where().ifExists();
            return delete;
        };
    }




}
