package com.jstik.fancy.account.search.service;

import com.jstik.fancy.account.search.dao.repository.elastic.user.UserTypeRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticServiceConfig {

    @Bean
    public IUserTypeService userTypeService(UserTypeRepository userTypeRepository){
        return new UserTypeService(userTypeRepository);
    }
}
