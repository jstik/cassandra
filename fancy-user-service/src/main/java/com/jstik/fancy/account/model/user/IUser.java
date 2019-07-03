package com.jstik.fancy.account.model.user;

import java.time.LocalDateTime;
import java.util.Set;

public interface IUser {

    String getLogin();

    String getFirstName();

    String getLastName();

    String getEmail();

    Set<String> getTags();

    Set<String> getGroups();

    LocalDateTime getCreated();


}
