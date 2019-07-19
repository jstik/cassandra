package com.jstik.site.fancy.elastic.test;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchProperties;

import java.util.ArrayList;
import java.util.Collection;

@Getter
@Setter
public class EmbeddedElasticsearchProperties extends ElasticsearchProperties {

    private String version;

    private EmbeddedProperties embedded = new EmbeddedProperties();


    @Getter
    @Setter
    public static class EmbeddedProperties{
        private String httpProxyHost;

        private int httpProxyPort;

        private String downloadDirectory;

        private int startTimeoutSeconds = 120;

        private Collection<String> plugins = new ArrayList<>();
    }
}
