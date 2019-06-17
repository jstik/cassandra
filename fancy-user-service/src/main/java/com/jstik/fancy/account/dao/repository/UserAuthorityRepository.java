package com.jstik.fancy.account.dao.repository;

import com.jstik.fancy.account.entity.user.UserAuthority;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;

public interface UserAuthorityRepository extends ReactiveCassandraRepository<UserAuthority, UserAuthority.UserAuthorityPrimaryKey> {
}
