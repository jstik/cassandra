package com.jstik.fancy.account.search;

import com.jstik.fancy.account.search.dao.repository.elastic.ElasticConfig;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

@Configuration
@Import(ElasticConfig.class)
@EnableConfigurationProperties({ElasticsearchProperties.class})
public class TestElasticConfig {


    @Bean
    @Primary
    public ElasticsearchProperties elasticsearchProperties() {
        return new ElasticsearchProperties();
    }
}
