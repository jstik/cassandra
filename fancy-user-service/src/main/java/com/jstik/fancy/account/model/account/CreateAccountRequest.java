package com.jstik.fancy.account.model.account;

import com.jstik.fancy.account.model.AuthorityDTO;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
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
    private Set<String> tags;
    private Set<String> groups;

    private List<AuthorityDTO> authorities = new ArrayList<>();
}
