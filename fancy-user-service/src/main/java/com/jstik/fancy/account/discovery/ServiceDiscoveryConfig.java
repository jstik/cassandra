package com.jstik.fancy.account.discovery;


import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@EnableDiscoveryClient
public class ServiceDiscoveryConfig {
   @Bean
   @Profile("dev")
    public LoadBalancerClient stubLoadBalancerClient(){
        return  new StubLoadBalancerClient();
    }

}
