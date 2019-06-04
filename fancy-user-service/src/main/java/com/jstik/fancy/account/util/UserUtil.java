package com.jstik.fancy.account.util;

import java.util.UUID;

public class UserUtil {

    public static String generateRegKey() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
