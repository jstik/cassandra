package com.jstik.fancy.chat.dao.repository;

import com.datastax.driver.core.DataType;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.jstik.fancy.chat.model.entity.Message;
import org.springframework.data.cassandra.core.ReactiveCassandraOperations;
import org.springframework.data.cassandra.core.mapping.BasicCassandraPersistentEntity;
import org.springframework.data.cassandra.core.mapping.CassandraMappingContext;
import reactor.core.publisher.Flux;

import javax.inject.Inject;
import java.time.LocalDate;


public class CustomMessageRepositoryImpl implements CustomMessageRepository {

    @Inject
    private ReactiveCassandraOperations operations;

    @Inject
    private CassandraMappingContext cassandraMapping;

    @Override
    public Flux<Message> findByMessagesByAddressAndDayCreatedBetween(String address, LocalDate start, LocalDate end) {
        BasicCassandraPersistentEntity<?> persistentEntity = cassandraMapping.getPersistentEntity(Message.class);
        QueryBuilder.select().from(persistentEntity.getTableName().toCql());



       return  null;
    }
}
