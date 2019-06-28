package com.jstik.fancy.account.search.dao.repository.elastic;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.TransportClientFactoryBean;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.EntityMapper;
import org.springframework.data.elasticsearch.core.geo.CustomGeoModule;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import javax.inject.Inject;
import java.io.IOException;
import java.net.InetAddress;
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
        return new ElasticsearchTemplate(client(), new CustomElasticEntityMapper());
    }

    public static class CustomElasticEntityMapper implements EntityMapper {

        private final ObjectMapper objectMapper;

        CustomElasticEntityMapper() {
            objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.registerModule(new CustomGeoModule());
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

        }

        @Override
        public String mapToString(Object object) throws IOException {
            return objectMapper.writeValueAsString(object);
        }

        @Override
        public <T> T mapToObject(String source, Class<T> clazz) throws IOException {
            return objectMapper.readValue(source, clazz);
        }
    }
}
