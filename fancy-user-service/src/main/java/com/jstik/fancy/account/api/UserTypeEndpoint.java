package com.jstik.fancy.account.api;

import com.jstik.fancy.account.search.entity.elastic.UserType;
import com.jstik.fancy.account.search.service.IUserTypeService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

@RestController
public class UserTypeEndpoint implements IUserTypeEndpoint {

    private IUserTypeService userTypeService;

    @Inject
    public UserTypeEndpoint(IUserTypeService userTypeService) {
        this.userTypeService = userTypeService;
    }


    @Override
    public ResponseEntity<Page<UserType>> getAllUsers(int page, int size) {
        return new ResponseEntity<>(
                userTypeService.getAllUsers(page,size)
                ,HttpStatus.OK);
    }

}
