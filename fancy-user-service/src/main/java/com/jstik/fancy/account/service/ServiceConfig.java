package com.jstik.fancy.account.service;

import com.jstik.fancy.account.dao.repository.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class ServiceConfig {


    private final ConversionService conversionService;
    private final PasswordEncoder passwordEncoder;

    public ServiceConfig(@Qualifier("mvcConversionService") ConversionService conversionService,
                         PasswordEncoder passwordEncoder) {
        this.conversionService = conversionService;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public UserService userService(UserRepository userRepository, UserOperationsRepository operationsRepository,
                                   TagService tagService,
                                   ClientService clientService,
                                   AuthorityService authorityService) {
        return new UserService(userRepository,
                operationsRepository, tagService, clientService,authorityService);
    }

    @Bean
    public AccountService accountService(UserService userService,
                                         UserRegistrationRepository registrationRepository,
                                         LoadBalancerClient loadBalancerClient) {
        return new AccountService(conversionService, userService, userRegistrationService(registrationRepository, loadBalancerClient));
    }

    @Bean
    public UserRegistrationService userRegistrationService(UserRegistrationRepository userRegistrationRepository, LoadBalancerClient loadBalancerClient) {
        return new UserRegistrationService(userRegistrationRepository, passwordEncoder, loadBalancerClient);
    }


}
