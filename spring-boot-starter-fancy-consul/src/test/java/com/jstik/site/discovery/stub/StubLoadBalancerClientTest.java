package com.jstik.site.discovery.stub;

import com.jstik.site.discovery.ConsulCustomTagAutoConfigurationApp;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;

import java.net.URI;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {ConfigFileApplicationContextInitializer.class,ConsulCustomTagAutoConfigurationApp.class})
@TestPropertySource("classpath:stub-on.properties")
public class StubLoadBalancerClientTest {

    @Inject
    private LoadBalancerClient loadBalancerClient;

    @Value("${spring.cloud.consul.stub.service.test-service.host}")
    private String testServiceHost;

    @Value("${spring.cloud.consul.stub.service.test-service.port}")
    private int testServicePort;

    @Test
    public void choose() throws Exception {
        ServiceInstance serviceInstance = loadBalancerClient.choose("test-service");
        Assert.assertNotNull(serviceInstance);
    }

    @Test
    public void execute() throws Exception {
    }

    @Test
    public void reconstructURI() throws Exception {
        ServiceInstance serviceInstance = loadBalancerClient.choose("test-service");
        URI uri = loadBalancerClient.reconstructURI(serviceInstance, URI.create("http://test-service/go-to"));
        Assert.assertThat(uri.getHost(), is(testServiceHost));
        Assert.assertThat(uri.getPort(), is(testServicePort));
    }

}