package com.jstik.fancy.account.storage.service;

import com.jstik.fancy.account.handler.operation.DMLOperationHandler;
import com.jstik.fancy.account.model.user.IUser;
import com.jstik.fancy.account.storage.dao.repository.cassandra.authority.AuthorityRepository;
import com.jstik.fancy.account.storage.dao.repository.cassandra.client.ClientRepository;
import com.jstik.fancy.account.storage.dao.repository.cassandra.client.UsersByClientRepository;
import com.jstik.fancy.account.storage.dao.repository.cassandra.tag.EntityByTagRepository;
import com.jstik.fancy.account.storage.dao.repository.cassandra.tag.TagRepository;
import com.jstik.fancy.account.storage.dao.repository.cassandra.user.UserRepository;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;

@Configuration
public class LinkedServicesConfig {

    @Bean
    public TagService tagService(TagRepository tagRepository, EntityByTagRepository entityByTagRepository) {
        return new TagService(tagRepository, entityByTagRepository);
    }

    @Bean
    public ClientService clientService(UsersByClientRepository usersByClientRepository, ClientRepository clientRepository) {
        return new ClientService(usersByClientRepository, clientRepository);
    }

    @Bean
    public AuthorityService authorityService(AuthorityRepository authorityRepository) {
        return new AuthorityService(authorityRepository);
    }


    @Bean
    public IUserService userService(UserRepository userRepository,
                                    ObjectProvider<Collection<DMLOperationHandler<IUser>>> dmlHandlers,
                                    TagService tagService,
                                    ClientService clientService,
                                    AuthorityService authorityService) {
        return new UserService(userRepository, tagService, clientService, authorityService, dmlHandlers);
    }

}
