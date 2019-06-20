package com.jstik.fancy.account.service;

import com.jstik.fancy.account.dao.repository.TagRepository;
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

    @Bean
    public UserService userService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                                   UserRegistrationRepository userRegistrationRepository,
                                   TagRepository tagRepository,
                                   LoadBalancerClient loadBalancerClient) {
        return  new UserService(userRepository, passwordEncoder,userRegistrationRepository, tagRepository, loadBalancerClient);
    }

    @Bean
    public AccountService accountService(@Qualifier("mvcConversionService") ConversionService conversionService,
                                         UserService userService,
                                         UserRegistrationRepository registrationRepository) {
        return new AccountService(conversionService, userService, registrationRepository);
    }
}
