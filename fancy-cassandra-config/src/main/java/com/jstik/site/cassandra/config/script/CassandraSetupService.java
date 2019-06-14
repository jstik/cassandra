package com.jstik.site.cassandra.config.script;


import com.jstik.site.cassandra.config.keyspace.KeyspaceProperties;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.io.Resource;

import java.util.Collections;
import java.util.List;

import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification;
import org.springframework.data.cassandra.core.cql.keyspace.DropKeyspaceSpecification;

import static com.jstik.site.cassandra.config.keyspace.KeyspaceSpecificationBuilder.from;

public class CassandraSetupService implements ICassandraSetupService {

    private KeyspaceProperties keyspaceProperties;

    private ConversionService conversionService;

    private Resource startupScript;

    private Resource shutdownScript;

    public CassandraSetupService(KeyspaceProperties keyspaceProperties,
                                 ConversionService conversionService,
                                 Resource startupScript,
                                 Resource shutdownScript) {
        this.keyspaceProperties = keyspaceProperties;
        this.conversionService = conversionService;
        this.startupScript = startupScript;
        this.shutdownScript = shutdownScript;
    }

    @Override
    public List<String> getStartupScripts() {
        if (startupScript == null)
            return Collections.emptyList();
        return ScriptsUtils.readScripts(startupScript);
    }


    @Override
    public List<String> getShutdownScripts() {
        if (shutdownScript == null)
            return Collections.emptyList();
        return ScriptsUtils.readScripts(shutdownScript);
    }

    @Override
    public SchemaAction getSchemaAction(String schemaAction) {
        if (schemaAction == null)
            return SchemaAction.NONE;
        SchemaAction result = conversionService.convert(schemaAction, SchemaAction.class);
        return result == null ? SchemaAction.NONE : result;
    }

    @Override
    public List<CreateKeyspaceSpecification> getKeyspaceCreations(String keyspace) {
        return from(keyspace).build(keyspaceProperties).createSpecificationsOrEmpty();
    }

    @Override
    public List<DropKeyspaceSpecification> getKeyspaceDrops(String keyspace) {
        return from(keyspace).build(keyspaceProperties).dropSpecificationsOrEmpty();
    }
}
