package com.jstik.fancy.account.storage.service;

import com.jstik.fancy.account.convertors.CreateAccountRequestToUser;
import com.jstik.fancy.account.storage.dao.repository.cassandra.user.UserRegistrationRepository;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class ServiceConfig {


    private final PasswordEncoder passwordEncoder;

    public ServiceConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }


    @Bean
    public AccountService accountService(UserService userService,
                                         UserRegistrationRepository registrationRepository,
                                         LoadBalancerClient loadBalancerClient) {
        return new AccountService(conversionService(), userService, userRegistrationService(registrationRepository, loadBalancerClient));
    }

    @Bean
    public UserRegistrationService userRegistrationService(UserRegistrationRepository userRegistrationRepository, LoadBalancerClient loadBalancerClient) {
        return new UserRegistrationService(userRegistrationRepository, passwordEncoder, loadBalancerClient);
    }

    @Bean("accountConversionService")
    public ConversionService conversionService() {
        DefaultConversionService service = new DefaultConversionService();
        service.addConverter(new CreateAccountRequestToUser());
        return service;
    }


}
