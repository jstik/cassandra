package com.jstik.fancy.account.convertors;

import com.jstik.fancy.account.search.entity.elastic.UserType;
import com.jstik.fancy.account.storage.entity.cassandra.user.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;

public class UserToUserType implements Converter<User, UserType> {
    @Nullable
    @Override
    public UserType convert(@NotNull User user) {
        UserType userType = new UserType(user.getLogin(), user.getFirstName(), user.getLastName(), user.getEmail());
        userType.setCreated(user.getCreated());
        userType.setTags(user.getTags());
        userType.setGroups(user.getGroups());
        return userType;
    }
}
