package com.jstik.fancy.user.model.account;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterAccountRequest {

    @NonNull
    private String login;
    @NonNull
    private String password;

    @NonNull
    private String regKey;
}