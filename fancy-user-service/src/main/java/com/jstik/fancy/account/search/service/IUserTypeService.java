package com.jstik.fancy.account.search.service;

import com.jstik.fancy.account.search.entity.elastic.UserType;
import org.springframework.data.domain.Page;

public interface IUserTypeService {
    void saveUserDocument(UserType userType);

    Page<UserType> getAllUsers(int page, int size);
}
