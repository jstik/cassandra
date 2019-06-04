package com.jstik.fancy.account.discovery;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

@RestController()
@RefreshScope
public class DiscoveryClientController {

    @Inject
    private LoadBalancerClient loadBalancerClient;

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${account.activation.url}")
    private String activationUrl;



    @GetMapping("/${account.activation.url}")
    public Object choose(){
        return activationUrl;
    }
}
