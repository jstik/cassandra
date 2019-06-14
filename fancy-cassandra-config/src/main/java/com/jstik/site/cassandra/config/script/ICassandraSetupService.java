package com.jstik.site.cassandra.config.script;

import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification;
import org.springframework.data.cassandra.core.cql.keyspace.DropKeyspaceSpecification;

import java.util.List;

public interface ICassandraSetupService {
    List<String> getStartupScripts();

    List<String> getShutdownScripts();

    SchemaAction getSchemaAction(String schemaAction);

    List<CreateKeyspaceSpecification> getKeyspaceCreations(String keyspace);

    List<DropKeyspaceSpecification> getKeyspaceDrops(String keyspace);
}
