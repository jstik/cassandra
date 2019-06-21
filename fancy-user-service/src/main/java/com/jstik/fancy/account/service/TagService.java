package com.jstik.fancy.account.service;

import com.jstik.fancy.account.dao.repository.TagRepository;
import com.jstik.fancy.account.entity.EntityWithDiscriminator;
import com.jstik.fancy.account.entity.tag.EntityByTag;
import com.jstik.fancy.account.entity.user.User;
import com.jstik.site.cassandra.statements.EntityAwareBatchStatement;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Optional;

import static com.jstik.site.cassandra.statements.DMLStatementProducerBuilder.insertProducer;

public class TagService {

    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public Mono<Boolean> addTagsForEntity(Collection<String> tags, EntityWithDiscriminator entity){
        if(tags == null || tags.isEmpty())
            return Mono.just(true);


        if(entity.getId() == null)
            return Mono.error(new IllegalStateException("Id must be not null!"));

        EntityAwareBatchStatement batch = entityByTagStatement(tags, entity).get();
        return tagRepository.executeBatch(batch).then(tagRepository.saveTags(tags));
    }


    private Optional<EntityAwareBatchStatement> entityByTagStatement(Collection<String> tags, EntityWithDiscriminator entity) {
        if(tags == null)
            return Optional.empty();
        return tags.stream()
                .map(tag -> new EntityAwareBatchStatement(insertProducer(), new EntityByTag(tag, entity.getId(), entity.getDiscriminator())))
                .reduce(EntityAwareBatchStatement::andThen);
    }
}
