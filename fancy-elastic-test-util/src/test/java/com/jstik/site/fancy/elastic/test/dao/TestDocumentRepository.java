package com.jstik.site.fancy.elastic.test.dao;

import com.jstik.site.fancy.elastic.test.entity.TestDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface TestDocumentRepository extends ElasticsearchRepository<TestDocument, String> {
}
