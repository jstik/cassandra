package com.jstik.fancy.user.convertors;

import com.jstik.fancy.user.entity.User;
import com.jstik.fancy.user.model.account.CreateAccountRequest;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;

public class CreateAccountRequestToUser implements Converter<CreateAccountRequest, User> {
    @Nullable
    @Override
    public User convert(@NotNull CreateAccountRequest account) {
        User user = new User(account.getLogin(), account.getFirstName(), account.getLastName(), account.getEmail());
        user.setClients(account.getClients());
        return user;
    }
}
