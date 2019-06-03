package com.jstik.fancy.user.discovery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.client.loadbalancer.ServiceInstanceChooser;

import javax.inject.Inject;

public class LoadBalancerClientWrapper implements ServiceInstanceChooser {


    private LoadBalancerClient client;

    public LoadBalancerClientWrapper( LoadBalancerClient client) {
        this.client = client;
    }

    @Override
    public ServiceInstance choose(String serviceId) {
        return client.choose(serviceId);
    }
}
