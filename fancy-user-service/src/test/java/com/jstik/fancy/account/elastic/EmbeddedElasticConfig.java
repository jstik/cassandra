package com.jstik.fancy.account.elastic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.util.UriComponentsBuilder;
import pl.allegro.tech.embeddedelasticsearch.EmbeddedElastic;
import pl.allegro.tech.embeddedelasticsearch.PopularProperties;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.concurrent.TimeUnit;

import static java.net.Proxy.Type.HTTP;

@Configuration
public class EmbeddedElasticConfig implements DisposableBean {

    Logger logger = LoggerFactory.getLogger(EmbeddedElasticConfig.class);


    private EmbeddedElastic embeddedElastic;

    public EmbeddedElasticConfig(ElasticsearchProperties properties) {
        try {
            embeddedElastic = startEmbeddedElastic(properties);
        }  catch (InterruptedException e) {
           if(embeddedElastic != null)
               embeddedElastic.stop();
        }catch (Exception e) {
            logger.error("Could't start embeddedElastic ", e);
        }


    }

    public EmbeddedElastic startEmbeddedElastic(ElasticsearchProperties properties) throws IOException, InterruptedException {
        return EmbeddedElastic.builder()
                .withElasticVersion("6.4.3")
                .withSetting(PopularProperties.TRANSPORT_TCP_PORT, getElasticPort(properties))
                .withSetting(PopularProperties.CLUSTER_NAME, properties.getClusterName())
                .withStartTimeout(60, TimeUnit.SECONDS)
                .withDownloadProxy(new Proxy(HTTP, new InetSocketAddress(InetAddress.getByName("10.7.200.56"), 36571)))
                .withDownloadDirectory(new File("G:\\distr\\embaded-elastic-source"))
                .build()
                .start();
    }

    private int getElasticPort(ElasticsearchProperties properties){
        String clusterNodes = properties.getClusterNodes();
        return UriComponentsBuilder.fromUriString("http://" + clusterNodes).build().getPort(); //todo!!!
    }

    @Override
    public void destroy() throws Exception {
        if(embeddedElastic != null)
        embeddedElastic.stop();
    }
}
