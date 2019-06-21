package com.jstik.fancy.account.dao.repository;

import com.jstik.fancy.account.entity.tag.Tag;
import com.jstik.site.cassandra.repository.CustomReactiveCassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Mono;

import java.util.Collection;

public interface TagRepository extends ReactiveCassandraRepository<Tag, String> , CustomReactiveCassandraRepository<Tag,String> {

    @Query("update tag SET counter=counter+1 WHERE name =:name ;")
    void saveTag(@Param("name") String name);


    @Query("update tag SET counter=counter+1 WHERE name in :tags ;")
    Mono<Boolean> saveTags(@Param("tags") Collection<String> tags);

}
