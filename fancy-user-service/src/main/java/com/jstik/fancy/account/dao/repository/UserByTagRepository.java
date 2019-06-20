package com.jstik.fancy.account.dao.repository;

import com.jstik.fancy.account.entity.tag.UserByTag;
import com.jstik.fancy.account.entity.tag.UserByTag.UserByTagPrimaryKey;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import reactor.core.publisher.Flux;

public interface UserByTagRepository  extends ReactiveCassandraRepository<UserByTag, UserByTagPrimaryKey> {


    Flux<UserByTag> findAllByPrimaryKeyTag(String tag);
}
