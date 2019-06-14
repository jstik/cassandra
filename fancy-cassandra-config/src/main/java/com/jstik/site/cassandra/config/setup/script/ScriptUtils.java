package com.jstik.site.cassandra.config.setup.script;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.lang.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.apache.commons.codec.Charsets.UTF_8;

public class ScriptUtils {

    public static List<String> readScripts(Resource resource,  @Nullable String commentPrefix ) {
        if (!resource.exists() || !resource.isFile())
            return Collections.emptyList();
        try {
            return readLines(resource, commentPrefix);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<String> readLines(Resource resource, @Nullable String commentPrefix ) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), UTF_8))) {
            Stream<String> lineStream = reader.lines().map(String::trim).filter(StringUtils::isNotBlank);
            if(commentPrefix != null)
                lineStream  = lineStream.filter(line-> !line.startsWith(commentPrefix));
            return lineStream.collect(Collectors.toList());
        }
    }

}
