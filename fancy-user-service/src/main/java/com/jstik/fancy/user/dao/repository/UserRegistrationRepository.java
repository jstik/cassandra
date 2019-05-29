package com.jstik.fancy.user.dao.repository;

import com.jstik.fancy.user.entity.UserRegistration;
import com.jstik.site.cassandra.repository.CustomReactiveCassandraRepository;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;

public interface UserRegistrationRepository extends ReactiveCassandraRepository<UserRegistration, String>, CustomReactiveCassandraRepository<UserRegistration, String> {

}
