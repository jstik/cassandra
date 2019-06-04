package com.jstik.fancy.account.model.account;

import com.jstik.fancy.account.entity.User;
import com.jstik.fancy.account.entity.UserRegistration;
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
}
