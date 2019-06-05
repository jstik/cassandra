package com.jstik.fancy.account.discovery.stub;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class StubLoadBalancerProperties {

    private final Map<String, ServiceInstanceProperties> service = new HashMap<>();

    @Getter
    @Setter
    public static class ServiceInstanceProperties{
        private String host;
        private int port;
        private boolean secure;
        private String instanceId;
    }

    public ServiceInstanceProperties getServiceInstanceProperties(String serviceId){
        return  this.service.get(serviceId);
    }
}
