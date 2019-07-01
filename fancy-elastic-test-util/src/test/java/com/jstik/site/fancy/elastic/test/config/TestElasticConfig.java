package com.jstik.site.fancy.elastic.test.config;

import com.jstik.site.fancy.elastic.test.EmbeddedElasticsearchProperties;
import org.elasticsearch.client.Client;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.TransportClientFactoryBean;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import java.util.Properties;

@Configuration
@EnableElasticsearchRepositories("com.jstik.site.fancy.elastic.test.dao")
public class TestElasticConfig {


}
