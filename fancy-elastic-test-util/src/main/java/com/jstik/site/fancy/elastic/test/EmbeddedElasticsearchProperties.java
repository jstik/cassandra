package com.jstik.site.fancy.elastic.test;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchProperties;

@Getter
@Setter
public class EmbeddedElasticsearchProperties extends ElasticsearchProperties {

    private String version;

    private String embeddedHttpProxyHost;

    private int embeddedHttpProxyPort;

    private String embeddedDownloadDirectory;
}
