package com.jstik.fancy.account.dao.repository;

import com.jstik.fancy.account.entity.tag.EntityByTag;
import com.jstik.fancy.account.entity.tag.EntityByTag.UserByTagPrimaryKey;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import reactor.core.publisher.Flux;

public interface EntityByTagRepository extends ReactiveCassandraRepository<EntityByTag, UserByTagPrimaryKey> {


    Flux<EntityByTag> findAllByPrimaryKeyTag(String tag);
}
