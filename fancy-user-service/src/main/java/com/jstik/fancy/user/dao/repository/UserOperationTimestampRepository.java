package com.jstik.fancy.user.dao.repository;

import com.jstik.fancy.user.entity.EntityOperation;
import com.jstik.fancy.user.entity.UserOperationTimestamp;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

public interface UserOperationTimestampRepository extends ReactiveCassandraRepository<UserOperationTimestamp, UserOperationTimestamp.UserOperationTimestampPrimaryKey> {


    Flux<UserOperationTimestamp> findAllByPrimaryKeyOperationAndPrimaryKeyTimestampAfter(EntityOperation operation, LocalDateTime after);
}


