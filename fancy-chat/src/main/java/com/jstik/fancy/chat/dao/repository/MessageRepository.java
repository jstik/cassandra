package com.jstik.fancy.chat.dao.repository;

import com.jstik.fancy.chat.model.entity.Message;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.util.Collection;

public interface MessageRepository  extends ReactiveCassandraRepository<Message, Message.MessagePrimaryKey>, CustomMessageRepository  {

    Flux<Message> findByMessageKeyAddressAndMessageKeyDayCreatedIn(String address, Collection<LocalDate> days);

    Flux<Message> findByMessageKeyAddressAndMessageKeyDayCreated(String address, LocalDate day);

}
