package com.jstik.fancy.account.service;

import com.jstik.fancy.account.entity.User;
import com.jstik.fancy.account.model.account.CreateAccountRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.core.convert.ConversionService;

import javax.inject.Inject;
import java.net.URI;

public class AccountOperationConversionComponent {

    @Inject private ConversionService conversionService;
    @Inject private LoadBalancerClient loadBalancerClient;

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${account.activation.url}")
    private String activationUrl;


    public User createAccountRequestToUser(CreateAccountRequest account){
        return  conversionService.convert(account, User.class);
    }

    public String regKeyToRegistrationUrl(String regKey){
        ServiceInstance serviceInstance = loadBalancerClient.choose(applicationName);
        URI uri = serviceInstance.getUri();
        return uri.toString();
    }

}
