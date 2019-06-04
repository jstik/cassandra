package com.jstik.fancy.account.dao.repository;

import com.jstik.site.cassandra.model.EntityOperation;
import com.jstik.fancy.account.entity.UserOperationTimestamp;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

public interface UserOperationTimestampRepository extends ReactiveCassandraRepository<UserOperationTimestamp, UserOperationTimestamp.UserOperationTimestampPrimaryKey> {


    Flux<UserOperationTimestamp> findAllByPrimaryKeyOperationAndPrimaryKeyTimestampAfter(EntityOperation operation, LocalDateTime after);
}


