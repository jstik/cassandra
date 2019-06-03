package com.jstik.fancy.user.discovery;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerRequest;
import org.springframework.cloud.client.loadbalancer.ServiceInstanceChooser;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URI;
import java.text.MessageFormat;

public class StubLoadBalancerClient implements ServiceInstanceChooser {

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

        return new DefaultServiceInstance(serviceId, serviceId, host, port, false);
    }
}
