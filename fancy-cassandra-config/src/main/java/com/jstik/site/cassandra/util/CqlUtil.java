package com.jstik.site.cassandra.util;

import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Update;
import org.springframework.data.cassandra.core.convert.CassandraConverter;
import org.springframework.data.cassandra.core.mapping.CassandraPersistentEntity;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.datastax.driver.core.querybuilder.QueryBuilder.set;

public class CqlUtil {

    public static Insert createIfNotExistsInsert(CassandraConverter converter, Object entity) {

        CassandraPersistentEntity<?> persistentEntity = converter.getMappingContext().getRequiredPersistentEntity(entity.getClass());

        Map<String, Object> toInsert = new LinkedHashMap<>();

        converter.write(entity, toInsert, persistentEntity);

        Insert insert = QueryBuilder.insertInto(persistentEntity.getTableName().toCql());
        insert.ifNotExists();

        for (Map.Entry<String, Object> entry : toInsert.entrySet()) {
            insert.value(entry.getKey(), entry.getValue());
        }

        return insert;
    }


    public static Update createIfExistsUpdate(CassandraConverter converter, Object entity){
        CassandraPersistentEntity<?> persistentEntity = converter.getMappingContext().getRequiredPersistentEntity(entity.getClass());
        Update update = QueryBuilder.update(persistentEntity.getTableName().toCql());
        converter.write(entity, update, persistentEntity);
        update.where().ifExists();
        return  update;
    }
}