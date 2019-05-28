package com.jstik.fancy.chat.dao.repository;

import com.jstik.fancy.chat.model.entity.Message;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

public interface CustomMessageRepository {

  Flux<Message> findByMessagesByAddressAndDayCreatedBetween(String address, LocalDate start, LocalDate end);
}
