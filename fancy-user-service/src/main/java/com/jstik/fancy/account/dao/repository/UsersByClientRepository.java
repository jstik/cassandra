package com.jstik.fancy.account.dao.repository;

import com.jstik.fancy.account.entity.client.UsersByClient;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;

public interface UsersByClientRepository extends ReactiveCassandraRepository<UsersByClient, String> {
}
