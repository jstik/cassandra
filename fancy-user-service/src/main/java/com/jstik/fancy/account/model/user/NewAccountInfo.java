package com.jstik.fancy.account.model.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NewAccountInfo {

    private String regKey;

    private String regLink;

    public NewAccountInfo(String regKey) {
        this.regKey = regKey;
    }
}
