package com.jstik.fancy.account.search.service;

import com.jstik.fancy.account.search.dao.repository.elastic.user.UserTypeRepository;
import com.jstik.fancy.account.search.entity.elastic.UserType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import javax.inject.Inject;

import static org.springframework.data.domain.Sort.Order.desc;

public class UserTypeService {

    private final UserTypeRepository userTypeRepository;

    @Inject
    public UserTypeService(UserTypeRepository userTypeRepository) {
        this.userTypeRepository = userTypeRepository;
    }

    public void saveUserDocument(UserType userType) {
        userTypeRepository.save(userType);
    }

    public Page<UserType> getAllUsers(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(desc("updated"), desc("created")));
        return userTypeRepository.findAll(pageRequest);
    }
}
