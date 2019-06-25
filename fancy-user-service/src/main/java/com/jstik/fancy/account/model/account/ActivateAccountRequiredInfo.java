package com.jstik.fancy.account.model.account;

import com.jstik.fancy.account.entity.cassandra.user.User;
import com.jstik.fancy.account.entity.cassandra.user.UserRegistration;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ActivateAccountRequiredInfo {

    private User user;

    private UserRegistration userRegistration;

    private String plainPassword;
}
