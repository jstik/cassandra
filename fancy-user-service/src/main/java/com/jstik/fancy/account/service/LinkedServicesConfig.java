package com.jstik.fancy.account.service;

import com.jstik.fancy.account.dao.repository.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LinkedServicesConfig {

    @Bean
    public TagService tagService(TagRepository tagRepository, EntityByTagRepository entityByTagRepository) {
        return new TagService(tagRepository, entityByTagRepository);
    }

    @Bean
    public ClientService clientService(UsersByClientRepository usersByClientRepository, ClientRepository clientRepository){
        return new ClientService(usersByClientRepository,clientRepository);
    }

    @Bean
    public AuthorityService authorityService(AuthorityRepository authorityRepository){
        return new AuthorityService(authorityRepository);
    }
}
