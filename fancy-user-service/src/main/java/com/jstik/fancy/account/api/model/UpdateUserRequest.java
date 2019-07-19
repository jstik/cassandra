package com.jstik.fancy.account.api.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UpdateUserRequest {

    private String firstName;

    private String lastName;

    @Email
    private String email;
}
