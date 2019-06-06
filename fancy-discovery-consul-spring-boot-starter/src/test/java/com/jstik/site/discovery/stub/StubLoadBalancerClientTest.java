package com.jstik.site.discovery.stub;

import com.jstik.site.discovery.ConsulCustomTagAutoConfigurationApp;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {ConfigFileApplicationContextInitializer.class,ConsulCustomTagAutoConfigurationApp.class})
@TestPropertySource("classpath:stub-on.properties")
public class StubLoadBalancerClientTest {

    @Inject
    private LoadBalancerClient loadBalancerClient;

    @Test
    public void choose() throws Exception {

        ServiceInstance serviceInstance = loadBalancerClient.choose("test-service");
        Assert.assertNotNull(serviceInstance);
    }

}