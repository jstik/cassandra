package com.jstik.site.fancy.elastic.test;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchProperties;

@Getter
@Setter
public class EmbeddedElasticsearchProperties extends ElasticsearchProperties {

    private String version;

    private EmbeddedProperties embedded;


    @Getter
    @Setter
    public static class EmbeddedProperties{
        private String httpProxyHost;

        private int httpProxyPort;

        private String downloadDirectory;
    }
}
