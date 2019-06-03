package com.jstik.fancy.user.discovery;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.client.loadbalancer.ServiceInstanceChooser;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@EnableDiscoveryClient
public class ServiceDiscoveryConfig {


   @Bean
   @Profile("stubLoadBalancer")
    public ServiceInstanceChooser stubLoadBalancerClient(){
        return  new StubLoadBalancerClient();
    }

    @Bean
    @Profile("!stubLoadBalancer")
    public ServiceInstanceChooser loadBalancerClient( LoadBalancerClient clien){
        return  new LoadBalancerClientWrapper(clien);
    }
}
