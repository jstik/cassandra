package com.jstik.site.fancy.elastic.test.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Getter
@Setter
@NoArgsConstructor
@Document(indexName = "test", type = "testDocument")
public class TestDocument {

    @Id
    private String id;

    private String content;
}
