package com.jstik.fancy.user.model.account;

import com.jstik.fancy.user.entity.User;
import com.jstik.fancy.user.entity.UserRegistration;
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
