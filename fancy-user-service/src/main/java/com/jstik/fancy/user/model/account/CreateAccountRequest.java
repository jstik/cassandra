package com.jstik.fancy.user.model.account;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Getter
@Setter
public class CreateAccountRequest {

    @NotBlank
    private String login;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @Email
    private String email;

    private Set<String> clients;
}
