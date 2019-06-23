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

    public ServiceConfig(@Qualifier("mvcConversionService") ConversionService conversionService,
                         PasswordEncoder passwordEncoder) {
        this.conversionService = conversionService;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public UserService userService(UserRepository userRepository, UserOperationsRepository operationsRepository,
                                   UserRegistrationRepository registrationRepository,
                                   TagRepository tagRepository,
                                   LoadBalancerClient loadBalancerClient) {
        return new UserService(userRepository, userRegistrationService(registrationRepository, loadBalancerClient), operationsRepository, tagService(tagRepository));
    }

    @Bean
    public AccountService accountService(UserService userService,
                                         UserRegistrationRepository registrationRepository,
                                         LoadBalancerClient loadBalancerClient) {
        return new AccountService(conversionService, userService, userRegistrationService(registrationRepository,loadBalancerClient));
    }

    @Bean
    public UserRegistrationService userRegistrationService(UserRegistrationRepository userRegistrationRepository, LoadBalancerClient loadBalancerClient) {
        return new UserRegistrationService(userRegistrationRepository, passwordEncoder, loadBalancerClient);
    }

    @Bean
    public TagService tagService(TagRepository tagRepository){
        return new TagService(tagRepository, null);
    }
}
