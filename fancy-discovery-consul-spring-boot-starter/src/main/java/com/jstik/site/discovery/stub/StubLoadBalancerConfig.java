package com.jstik.site.discovery.stub;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.consul.serviceregistry.ConsulRegistrationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConditionalOnProperty(
        name = {"spring.cloud.consul.stub.enabled"}
)
public class StubLoadBalancerConfig {

    @Bean
    public LoadBalancerClient stubLoadBalancerClient(ObjectProvider<List<ConsulRegistrationCustomizer>> registrationCustomizers){
        return  new StubLoadBalancerClient(stubLoadBalancerProperties(), registrationCustomizers);
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.cloud.consul.stub")
    public StubLoadBalancerProperties stubLoadBalancerProperties(){
        return new StubLoadBalancerProperties();
    }

}
