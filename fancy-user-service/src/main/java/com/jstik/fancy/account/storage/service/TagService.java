package com.jstik.fancy.account.storage.service;

import com.jstik.fancy.account.storage.dao.repository.cassandra.tag.EntityByTagRepository;
import com.jstik.fancy.account.storage.dao.repository.cassandra.tag.TagRepository;
import com.jstik.fancy.account.storage.entity.EntityWithDiscriminator;
import com.jstik.fancy.account.storage.entity.cassandra.tag.EntityByTag;
import com.jstik.site.cassandra.statements.EntityAwareBatchStatement;
import reactor.core.publisher.Mono;
import java.util.Collection;
import java.util.Optional;

import static com.jstik.site.cassandra.statements.DMLStatementProducerBuilder.insertProducer;

public class TagService {

    private final TagRepository tagRepository;

    private final EntityByTagRepository entityByTagRepository;

    public TagService(TagRepository tagRepository, EntityByTagRepository entityByTagRepository) {
        this.tagRepository = tagRepository;
        this.entityByTagRepository = entityByTagRepository;
    }

    public Mono<Boolean> addTagsForEntity(Collection<String> tags, EntityWithDiscriminator entity){
        if(tags == null || tags.isEmpty())
            return Mono.just(true);


        if(entity.getId() == null)
            return Mono.error(new IllegalStateException("Id must be not null!"));

        EntityAwareBatchStatement batch = insertEntityByTagStatement(tags, entity).get();
        return tagRepository.executeBatch(batch).then(tagRepository.saveTags(tags));
    }

    public Mono<Boolean> deleteTagsForEntity(Collection<String> tags, EntityWithDiscriminator entity){
        if(tags == null || tags.isEmpty())
            return Mono.just(true);
        if(entity.getId() == null)
            return Mono.error(new IllegalStateException("Id must be not null!"));
        return entityByTagRepository.deleteEntityByTags(entity.getId(), entity.getDiscriminator(), tags)
                .then(tagRepository.decrementTags(tags));
    }


    private Optional<EntityAwareBatchStatement> insertEntityByTagStatement(Collection<String> tags, EntityWithDiscriminator entity) {
        if(tags == null)
            return Optional.empty();
        return tags.stream()
                .map(tag -> new EntityAwareBatchStatement(insertProducer(), new EntityByTag(tag, entity.getId(), entity.getDiscriminator())))
                .reduce(EntityAwareBatchStatement::andThen);
    }
}
