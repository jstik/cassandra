package com.jstik.fancy.chat.dao.repository;

import com.jstik.fancy.chat.model.Message;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import reactor.core.publisher.Flux;

public interface MessageRepository  extends ReactiveCassandraRepository<Message, Message.MessagePK> {

    Flux<Message> findByMessagePKAddress(String address);
}
