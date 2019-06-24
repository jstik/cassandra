package com.jstik.fancy.account.dao.repository;

import com.jstik.fancy.account.entity.authority.Authority;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;

public interface AuthorityRepository extends ReactiveCassandraRepository<Authority, Authority.AuthorityPrimaryKey> {
}
