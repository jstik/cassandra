package com.jstik.fancy.user.discovery;


import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.ServiceInstanceChooser;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;
import java.text.MessageFormat;

public class LocalServiceInstanceChooser implements ServiceInstanceChooser {

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
