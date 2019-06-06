package com.jstik.site.discovery.tags;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;


@Getter
@Setter
public class ConsulCustomTagsProperties {

    private final Map<String, String> add = new HashMap<>();

    private boolean enabled;

}
