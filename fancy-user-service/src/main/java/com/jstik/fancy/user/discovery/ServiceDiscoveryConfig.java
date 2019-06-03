package com.jstik.fancy.user.discovery;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.ServiceInstanceChooser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableDiscoveryClient
public class ServiceDiscoveryConfig {


    @Bean
    public ServiceInstanceChooser serviceInstanceChooser(){
        return  new LocalServiceInstanceChooser();
    }
}
