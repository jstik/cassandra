package com.jstik.site.fancy.elastic.test;

import org.junit.rules.ExternalResource;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.ElasticsearchPersistentEntity;
import org.springframework.data.elasticsearch.core.mapping.ElasticsearchPersistentProperty;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.util.TypeInformation;

import java.util.Objects;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public class ElasticDropIndexesBeforeRule extends ExternalResource {

    private ElasticsearchOperations elasticsearchOperations;
    private MappingContext<? extends ElasticsearchPersistentEntity<?>, ElasticsearchPersistentProperty> mappingContext;

    public ElasticDropIndexesBeforeRule(ElasticsearchOperations elasticsearchOperations) {
        this.elasticsearchOperations = elasticsearchOperations;
        this.mappingContext = elasticsearchOperations.getElasticsearchConverter().getMappingContext();
    }


    protected void before(){
        Set<? extends Class<?>> types = this.mappingContext.getManagedTypes().stream()
                .map(TypeInformation::getType).collect(toSet());
        Set<? extends ElasticsearchPersistentEntity<?>> entities = types.stream()
                .map(type -> this.mappingContext.getRequiredPersistentEntity(type))
                .filter(Objects::nonNull).collect(toSet());

        Set<String> indexes = entities.stream().map(ElasticsearchPersistentEntity::getIndexName).collect(toSet());

        indexes.forEach(index->{
            this.elasticsearchOperations.deleteIndex(index);
        });
    }

}
