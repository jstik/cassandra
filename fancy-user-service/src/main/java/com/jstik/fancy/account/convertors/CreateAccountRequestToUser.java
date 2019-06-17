package com.jstik.fancy.account.convertors;

import com.jstik.fancy.account.entity.user.User;
import com.jstik.fancy.account.model.account.CreateAccountRequest;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;

public class CreateAccountRequestToUser implements Converter<CreateAccountRequest, User> {
    @Nullable
    @Override
    public User convert(@NotNull CreateAccountRequest account) {
        User user = new User(account.getLogin(), account.getFirstName(), account.getLastName(), account.getEmail());
        user.setClients(account.getClients());
        user.setGroups(account.getGroups());
        user.setTags(account.getTags());
        return user;
    }
}
