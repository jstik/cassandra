package com.jstik.fancy.account.service;

import com.jstik.fancy.account.dao.repository.TagRepository;
import com.jstik.fancy.account.entity.EntityWithDiscriminator;
import reactor.core.publisher.Mono;

import java.util.Collection;

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

        return Mono.just(true);
    }
}
