package com.jstik.fancy.account.discovery;


import com.jstik.fancy.account.discovery.stub.StubLoadBalancerClient;
import com.jstik.fancy.account.discovery.stub.StubLoadBalancerProperties;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.consul.serviceregistry.ConsulRegistrationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.List;

@Configuration
@EnableDiscoveryClient
public class ServiceDiscoveryConfig {
   @Bean
   @Profile("dev")
    public LoadBalancerClient stubLoadBalancerClient(ObjectProvider<List<ConsulRegistrationCustomizer>> registrationCustomizers){
        return  new StubLoadBalancerClient(stubLoadBalancerProperties(), registrationCustomizers);
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.cloud.consul.stub")
    public StubLoadBalancerProperties stubLoadBalancerProperties(){
        return new StubLoadBalancerProperties();
    }


    @Bean
    @ConfigurationProperties(prefix = "spring.cloud.consul.custom")
    public ConsulCustomTagsProperties consulCustomTagsProperties(){
        return new ConsulCustomTagsProperties();
    }

    @Bean
    public CustomTagsConsulCustomizer consulCustomizer(ObjectProvider<ConsulCustomTagsProperties> tagsProvider){
        return new CustomTagsConsulCustomizer(tagsProvider);
    }

}
