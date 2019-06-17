package com.jstik.site.cassandra.config.setup.script;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CassandraScriptsProperties {

    private String startupScript;

    private String shutdownScript;

    private boolean enabled;
}
