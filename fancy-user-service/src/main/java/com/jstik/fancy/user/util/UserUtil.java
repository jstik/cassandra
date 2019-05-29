package com.jstik.fancy.user.util;

import java.util.UUID;

public class UserUtil {

    public static String generateRegKey() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
