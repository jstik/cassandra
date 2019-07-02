package com.jstik.fancy.account.search.dao.repository.elastic;

import com.jstik.site.fancy.elastic.config.ElasticSearchConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.jstik.fancy.account.search.dao.repository.elastic.*")
@Import(ElasticSearchConfig.class)
public class ElasticConfig {
}
