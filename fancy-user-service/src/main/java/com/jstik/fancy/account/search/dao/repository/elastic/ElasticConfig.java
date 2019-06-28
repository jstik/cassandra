package com.jstik.fancy.account.search.dao.repository.elastic;

import org.elasticsearch.client.Client;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.TransportClientFactoryBean;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import javax.inject.Inject;
import java.util.Properties;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.jstik.fancy.account.search.dao.repository.elastic.*")
public class ElasticConfig {

    private final ElasticsearchProperties properties;

    @Inject
    public ElasticConfig(ElasticsearchProperties properties) {
        this.properties = properties;
    }


    @Bean
    public Client client() throws Exception {
        Properties properties = new Properties();
        properties.put("cluster.name", this.properties.getClusterName());
        properties.putAll(this.properties.getProperties());
        TransportClientFactoryBean factory = new TransportClientFactoryBean();
        String clusterNodes = this.properties.getClusterNodes();
        if (clusterNodes != null)
            factory.setClusterNodes(clusterNodes);
        factory.setProperties(properties);
        factory.afterPropertiesSet();
        return factory.getObject();
    }

    @Bean
    public ElasticsearchOperations elasticsearchTemplate() throws Exception {
        return new ElasticsearchTemplate(client());
    }
}
