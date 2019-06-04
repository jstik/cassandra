package com.jstik.fancy.account.discovery;


import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerRequest;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URI;
import java.text.MessageFormat;

public class StubLoadBalancerClient implements LoadBalancerClient {

    @Resource
    private Environment environment;

    @Override
    public ServiceInstance choose(String serviceId) {
        String host = environment.getProperty(serviceId + ".host");
        if (host == null) {
            throw new IllegalArgumentException(MessageFormat.format("Check settings for {0}", serviceId));
        }
        Integer port;
        try {
            port = Integer.parseInt(environment.getProperty(serviceId + ".port"));
        } catch (Exception e) {
            throw new IllegalArgumentException(MessageFormat.format("Check settings for {0}", serviceId), e);
        }

        return new DefaultServiceInstance(null,serviceId, host, port, false);
    }

    @Override
    public <T> T execute(String serviceId, LoadBalancerRequest<T> request) throws IOException {
        return null;
    }

    @Override
    public <T> T execute(String serviceId, ServiceInstance serviceInstance, LoadBalancerRequest<T> request) throws IOException {
        return null;
    }

    @Override
    public URI reconstructURI(ServiceInstance instance, URI original) {
        return null;
    }
}
