package com.jstik.fancy.account.search.service;

import com.jstik.fancy.account.search.dao.repository.elastic.user.UserTypeRepository;
import com.jstik.fancy.account.search.entity.elastic.UserType;

import javax.inject.Inject;

public class UserTypeService {

    private final UserTypeRepository userTypeRepository;

    @Inject
    public UserTypeService(UserTypeRepository userTypeRepository) {
        this.userTypeRepository = userTypeRepository;
    }

    public void  addUserDocument(UserType userType){
        userTypeRepository.save(userType);
    }
}
