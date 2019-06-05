package com.jstik.fancy.account.discovery;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;


@Getter
@Setter
public class ConsulCustomTagsProperties {

    private final Map<String, String> tags = new HashMap<>();

}
