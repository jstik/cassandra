package com.jstik.fancy.user.service;

import com.jstik.fancy.user.dao.repository.UserRegistrationRepository;
import com.jstik.fancy.user.dao.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class ServiceConfig {

    @Bean
    public UserService userService(ConversionService conversionService, UserRepository userRepository,
                                   UserRegistrationRepository userRegistrationRepository,
                                   PasswordEncoder passwordEncoder){
        return  new UserService(conversionService, userRepository,userRegistrationRepository, passwordEncoder);
    }
}
