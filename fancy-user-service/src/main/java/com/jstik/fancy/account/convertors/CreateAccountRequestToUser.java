package com.jstik.fancy.account.convertors;

import com.jstik.fancy.account.storage.entity.cassandra.user.User;
import com.jstik.fancy.account.storage.entity.cassandra.authority.Authority;
import com.jstik.fancy.account.model.account.CreateAccountRequest;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;

import static java.util.stream.Collectors.toList;

public class CreateAccountRequestToUser implements Converter<CreateAccountRequest, User> {
    @Nullable
    @Override
    public User convert(@NotNull CreateAccountRequest account) {
        User user = new User(account.getLogin(), account.getFirstName(), account.getLastName(), account.getEmail());
        user.setClients(account.getClients());
        user.setGroups(account.getGroups());
        user.setTags(account.getTags());
        if(account.getAuthorities() != null)
            user.setAuthorities(account.getAuthorities().stream().map(dto-> new Authority(dto, user)).collect(toList()));
        return user;
    }
}
