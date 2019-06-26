package com.jstik.fancy.account.storage.dao.repository.cassandra.client;

import com.jstik.fancy.account.storage.entity.cassandra.client.Client;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;

public interface ClientRepository extends ReactiveCassandraRepository<Client, String> {
}
