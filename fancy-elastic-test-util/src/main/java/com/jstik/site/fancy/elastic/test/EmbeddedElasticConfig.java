package com.jstik.site.fancy.elastic.test;

import com.jstik.site.fancy.elastic.test.EmbeddedElasticsearchProperties.EmbeddedProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.util.UriComponentsBuilder;
import pl.allegro.tech.embeddedelasticsearch.EmbeddedElastic;
import pl.allegro.tech.embeddedelasticsearch.EmbeddedElastic.Builder;
import pl.allegro.tech.embeddedelasticsearch.PopularProperties;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import static java.net.InetAddress.getByName;
import static java.net.Proxy.Type.HTTP;

@Configuration
@EnableConfigurationProperties({EmbeddedElasticsearchProperties.class})
public class EmbeddedElasticConfig implements DisposableBean {

    private Logger logger = LoggerFactory.getLogger(EmbeddedElasticConfig.class);


    private EmbeddedElastic embeddedElastic;

    public EmbeddedElasticConfig(EmbeddedElasticsearchProperties properties) {
        try {
            embeddedElastic = startEmbeddedElastic(properties);
        } catch (InterruptedException e) {
            if (embeddedElastic != null)
                embeddedElastic.stop();
        } catch (Exception e) {
            logger.error("Could't start embeddedElastic ", e);
        }


    }

    @Bean
    @Primary
    public EmbeddedElasticsearchProperties embeddedElasticsearchProperties() {
        return new EmbeddedElasticsearchProperties();
    }

    private EmbeddedElastic startEmbeddedElastic(EmbeddedElasticsearchProperties properties) throws IOException, InterruptedException {
        Builder builder = EmbeddedElastic.builder()
                .withElasticVersion(properties.getVersion())
                .withSetting(PopularProperties.TRANSPORT_TCP_PORT, getElasticPort(properties))
                .withSetting(PopularProperties.CLUSTER_NAME, properties.getClusterName())
                .withStartTimeout(60, TimeUnit.SECONDS);
        EmbeddedProperties embedded = properties.getEmbedded();
        if (embedded != null) {
            String proxyHost = embedded.getHttpProxyHost();
            if (proxyHost != null) {
                int proxyPort = embedded.getHttpProxyPort();
                builder = builder.withDownloadProxy(new Proxy(HTTP, new InetSocketAddress(getByName(proxyHost), proxyPort)));
            }
            String downloadDirectory = embedded.getDownloadDirectory();
            if (downloadDirectory != null) {
                builder = builder.withDownloadDirectory(new File(downloadDirectory));
            }
        }
        return builder.build().start();
    }

    private int getElasticPort(ElasticsearchProperties properties) {
        String clusterNodes = properties.getClusterNodes();
        return UriComponentsBuilder.fromUriString("http://" + clusterNodes).build().getPort(); //todo!!!
    }

    @Override
    public void destroy() throws Exception {
        if (embeddedElastic != null)
            embeddedElastic.stop();
    }
}
