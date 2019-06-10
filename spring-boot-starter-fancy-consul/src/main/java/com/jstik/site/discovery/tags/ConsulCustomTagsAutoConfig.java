package com.jstik.site.discovery.tags;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ConditionalOnProperty(
        name = {"spring.cloud.consul.custom.tags.enabled"},
        matchIfMissing = true
)
@Configuration
public class ConsulCustomTagsAutoConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.cloud.consul.custom.tags")
    public ConsulCustomTagsProperties consulCustomTagsProperties(){
        return new ConsulCustomTagsProperties();
    }

    @Bean
    public ConsulCustomTagsCustomizer consulCustomizer(ObjectProvider<ConsulCustomTagsProperties> tagsProvider){
        return new ConsulCustomTagsCustomizer(tagsProvider);
    }
}
