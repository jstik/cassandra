package com.jstik.fancy.account.model.authority;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthorityDTO {

    private String client;

    private String authority;

    private AccessLevel accessLevel;
}
