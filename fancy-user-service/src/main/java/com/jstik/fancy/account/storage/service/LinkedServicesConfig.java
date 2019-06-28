package com.jstik.fancy.account.storage.service;

import com.jstik.fancy.account.storage.dao.repository.cassandra.client.ClientRepository;
import com.jstik.fancy.account.storage.dao.repository.cassandra.client.UsersByClientRepository;
import com.jstik.fancy.account.storage.dao.repository.cassandra.tag.EntityByTagRepository;
import com.jstik.fancy.account.storage.dao.repository.cassandra.tag.TagRepository;
import com.jstik.fancy.account.storage.dao.repository.cassandra.authority.AuthorityRepository;
import com.jstik.fancy.account.storage.dao.repository.cassandra.user.UserOperationsRepository;
import com.jstik.fancy.account.storage.dao.repository.cassandra.user.UserRepository;
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


    @Bean
    public UserService userService(UserRepository userRepository, UserOperationsRepository operationsRepository,
                                   TagService tagService,
                                   ClientService clientService,
                                   AuthorityService authorityService) {
        return new UserService(userRepository, operationsRepository, tagService, clientService, authorityService);
    }

}