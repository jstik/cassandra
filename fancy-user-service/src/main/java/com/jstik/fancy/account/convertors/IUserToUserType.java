package com.jstik.fancy.account.convertors;

import com.jstik.fancy.account.model.user.IUser;
import com.jstik.fancy.account.search.entity.elastic.UserType;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;

public class IUserToUserType implements Converter<IUser, UserType> {
    @Nullable
    @Override
    public UserType convert(@NotNull IUser user) {
        UserType userType = new UserType(user.getLogin(), user.getFirstName(), user.getLastName(), user.getEmail());
        userType.setCreated(user.getCreated());
        userType.setTags(user.getTags());
        userType.setGroups(user.getGroups());
        return userType;
    }
}
