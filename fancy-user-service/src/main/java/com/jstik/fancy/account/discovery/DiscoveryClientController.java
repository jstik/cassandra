package com.jstik.fancy.account.discovery;

import com.jstik.fancy.account.discovery.stub.StubLoadBalancerProperties;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.net.URISyntaxException;

@RestController()
@RefreshScope
public class DiscoveryClientController {

    @Inject
    private LoadBalancerClient loadBalancerClient;
    @Inject
    private ConsulCustomTagsProperties customTagsProperties;
    @Inject
    StubLoadBalancerProperties stubLoadBalancerProperties;

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${account.activation.url}")
    private String activationUrl;



    @GetMapping("/${account.activation.url}")
    public Object choose() throws URISyntaxException {
        ServiceInstance instance = loadBalancerClient.choose(applicationName);
        String contextPath = instance.getMetadata().get("contextPath");

        URIBuilder builder = new URIBuilder(instance.getUri());

        builder.setPathSegments(contextPath, activationUrl);
        return builder.build();
    }
}
