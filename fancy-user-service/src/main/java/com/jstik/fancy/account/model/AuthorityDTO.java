package com.jstik.fancy.account.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthorityDTO {

    private String client;

    private String authority;

    private AccessLevel accessLevel;
}
