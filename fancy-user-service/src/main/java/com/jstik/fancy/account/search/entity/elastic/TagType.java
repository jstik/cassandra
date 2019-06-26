package com.jstik.fancy.account.search.entity.elastic;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Getter
@Setter
@Document(indexName = "account-service", type = "user")
public class TagType {

    @Id
    private String name;

    private Long counter;
}
