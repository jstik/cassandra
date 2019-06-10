package com.jstik.site.discovery.stub;


import com.ecwid.consul.v1.agent.model.NewService;
import com.jstik.site.discovery.stub.StubLoadBalancerProperties.ServiceInstanceProperties;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerRequest;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.cloud.commons.util.InetUtilsProperties;
import org.springframework.cloud.consul.discovery.ConsulDiscoveryProperties;
import org.springframework.cloud.consul.serviceregistry.ConsulRegistration;
import org.springframework.cloud.consul.serviceregistry.ConsulRegistrationCustomizer;

import javax.inject.Inject;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class StubLoadBalancerClient implements LoadBalancerClient {

    private final StubLoadBalancerProperties stubLoadBalancerProperties;

    private final ObjectProvider<List<ConsulRegistrationCustomizer>> registrationCustomizers;

    @Inject
    public StubLoadBalancerClient(StubLoadBalancerProperties stubLoadBalancerProperties, ObjectProvider<List<ConsulRegistrationCustomizer>> registrationCustomizers) {
        this.stubLoadBalancerProperties = stubLoadBalancerProperties;
        this.registrationCustomizers = registrationCustomizers;
    }

    @Override
    public ServiceInstance choose(String serviceId) {
        ServiceInstanceProperties stubProperties = stubLoadBalancerProperties.getServiceInstanceProperties(serviceId);
        if (stubProperties == null) {
            throw new IllegalArgumentException(MessageFormat.format("Check settings for {0}", serviceId));
        }
        List<ConsulRegistrationCustomizer> registrations = registrationCustomizers.getIfAvailable();
        Map<String, String> metadata = new LinkedHashMap<>();
        if (registrations != null) {
            ConsulDiscoveryProperties emptyProperties = new ConsulDiscoveryProperties(new InetUtils(new InetUtilsProperties()));
            ConsulRegistration consulRegistration = new ConsulRegistration(new NewService(), emptyProperties);
            registrations.forEach(reg -> reg.customize(consulRegistration));
            metadata = consulRegistration.getMetadata();
        }

        return new DefaultServiceInstance(stubProperties.getInstanceId(), serviceId, stubProperties.getHost(), stubProperties.getPort(), stubProperties.isSecure(), metadata);
    }

    @Override
    public <T> T execute(String serviceId, LoadBalancerRequest<T> request) throws IOException {
        ServiceInstance serviceInstance = choose(serviceId);
        try {
            return request.apply(serviceInstance);
        } catch (IOException e) {
            throw e;
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T execute(String serviceId, ServiceInstance serviceInstance, LoadBalancerRequest<T> request) throws IOException {
        try {
            return request.apply(serviceInstance);
        } catch (IOException e) {
            throw e;
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public URI reconstructURI(ServiceInstance instance, URI original) {
        String host = instance.getHost();
        int port = instance.getPort();
        String scheme = instance.getScheme() == null ? original.getScheme() : instance.getScheme();
        if (host.equals(original.getHost()) && port == original.getPort() && Objects.equals(scheme, original.getScheme()))
            return original;
        try {
            return new URI(createURIString(host, scheme, port, original));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private String createURIString(String host, String scheme, int port , URI original){
        StringBuilder sb = new StringBuilder();
        sb.append(scheme).append("://");
        if (!StringUtils.isBlank(original.getRawUserInfo()))
            sb.append(original.getRawUserInfo()).append("@");

        sb.append(host);
        if (port >= 0)
            sb.append(":").append(port);

        sb.append(original.getRawPath());
        if (!StringUtils.isBlank(original.getRawQuery()))
            sb.append("?").append(original.getRawQuery());

        if (!StringUtils.isBlank(original.getRawFragment()))
            sb.append("#").append(original.getRawFragment());

        return sb.toString();
    }
}
