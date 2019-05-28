package com.jstik.fancy.user.dao.repository;

import com.jstik.fancy.user.entity.User;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;

interface UserRepository extends ReactiveCassandraRepository<User, String>, CustomReactiveCassandraRepository<User, String> {
}
