package com.jstik.site.cassandra.config.setup.script;

import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.codec.Charsets.UTF_8;

public class ScriptsUtils {

    public static List<String> readScripts(Resource resource) {
        if (!resource.exists() || !resource.isFile())
            return Collections.emptyList();
        try {
            return readLines(resource);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<String> readLines(Resource resource) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), UTF_8))) {
            return reader.lines().collect(Collectors.toList());
        }
    }

}
