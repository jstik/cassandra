package com.jstik.fancy.account.dao.repository;

import com.jstik.fancy.account.entity.client.Client;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;

public interface ClientRepository extends ReactiveCassandraRepository<Client, String> {
}