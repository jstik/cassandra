package com.jstik.fancy.account.dao.repository.cassandra.tag;

import com.jstik.fancy.account.entity.cassandra.tag.EntityByTag;
import com.jstik.fancy.account.entity.cassandra.tag.EntityByTag.UserByTagPrimaryKey;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;

public interface EntityByTagRepository extends ReactiveCassandraRepository<EntityByTag, UserByTagPrimaryKey> {


    Flux<EntityByTag> findAllByPrimaryKeyTag(String tag);

    @Query("delete from entity_by_tag where entityId = :entityId and discriminator = :discriminator and tag in :tags")
    Mono<Boolean> deleteEntityByTags(@Param("entityId") String entityId,
                                     @Param("discriminator") String discriminator,
                                     @Param("tags") Collection<String> tags
    );
}
