package com.jstik.fancy.account.dao.repository.cassandra.user;

import com.jstik.fancy.account.entity.cassandra.user.UserRegistration;
import com.jstik.fancy.account.entity.cassandra.user.UserRegistration.UserRegistrationPrimaryKey;
import com.jstik.site.cassandra.repository.CustomReactiveCassandraRepository;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;

public interface UserRegistrationRepository extends ReactiveCassandraRepository<UserRegistration, UserRegistrationPrimaryKey>, CustomReactiveCassandraRepository<UserRegistration, UserRegistrationPrimaryKey> {

}
