package com.jstik.fancy.chat.dao.repository;

import com.jstik.fancy.chat.model.User;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;

public interface UserRepository extends ReactiveCassandraRepository<User, String> {
}
