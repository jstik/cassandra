package com.jstik.fancy.account.service;

import com.jstik.fancy.account.dao.repository.TagRepository;
import com.jstik.fancy.account.dao.repository.UserOperationsRepository;
import com.jstik.fancy.account.dao.repository.UserRegistrationRepository;
import com.jstik.fancy.account.dao.repository.UserRepository;
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

    private final LoadBalancerClient loadBalancerClient;

    public ServiceConfig(@Qualifier("mvcConversionService") ConversionService conversionService,
                         PasswordEncoder passwordEncoder,
                         LoadBalancerClient loadBalancerClient) {
        this.conversionService = conversionService;
        this.passwordEncoder = passwordEncoder;
        this.loadBalancerClient = loadBalancerClient;
    }

    @Bean
    public UserService userService(UserRepository userRepository, UserOperationsRepository operationsRepository,
                                   UserRegistrationRepository registrationRepository,
                                   TagRepository tagRepository) {
        return new UserService(userRepository, userRegistrationService(registrationRepository), operationsRepository, tagRepository);
    }

    @Bean
    public AccountService accountService(UserService userService,
                                         UserRegistrationRepository registrationRepository) {
        return new AccountService(conversionService, userService, userRegistrationService(registrationRepository));
    }

    @Bean
    public UserRegistrationService userRegistrationService(UserRegistrationRepository userRegistrationRepository) {
        return new UserRegistrationService(userRegistrationRepository, passwordEncoder, loadBalancerClient);
    }
}
