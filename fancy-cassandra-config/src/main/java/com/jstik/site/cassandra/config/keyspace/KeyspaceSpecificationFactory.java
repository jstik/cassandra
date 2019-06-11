package com.jstik.site.cassandra.config.keyspace;

import org.springframework.data.cassandra.core.cql.keyspace.*;
import org.springframework.data.cassandra.core.cql.keyspace.KeyspaceOption.ReplicationStrategy;
import org.springframework.data.cassandra.util.MapBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.springframework.data.cassandra.core.cql.keyspace.KeyspaceOption.DURABLE_WRITES;

public class KeyspaceSpecificationFactory {

    private List<DataCenterReplication> replications = new ArrayList<>();
    private ReplicationStrategy replicationStrategy = ReplicationStrategy.SIMPLE_STRATEGY;
    private long replicationFactor;
    private boolean durableWrites;
    private boolean ifNotExists;
    private String keyspaceName;

    KeyspaceSpecificationFactory(
            List<DataCenterReplication> replications, ReplicationStrategy replicationStrategy,
            long replicationFactor, boolean durableWrites,
            boolean ifNotExists, String keyspaceName
    ) {
        this.replications = replications;
        this.replicationStrategy = replicationStrategy;
        this.replicationFactor = replicationFactor;
        this.durableWrites = durableWrites;
        this.ifNotExists = ifNotExists;
        this.keyspaceName = keyspaceName;
    }

    public CreateKeyspaceSpecification create() {
        CreateKeyspaceSpecification specification = CreateKeyspaceSpecification.createKeyspace(keyspaceName);
        specification.ifNotExists(ifNotExists);
        specification.with(DURABLE_WRITES, durableWrites);
        specification.with(KeyspaceOption.REPLICATION, toOptions());
        return specification;
    }

    public AlterKeyspaceSpecification alter() {
        AlterKeyspaceSpecification specification = AlterKeyspaceSpecification.alterKeyspace(keyspaceName);
        specification.with(DURABLE_WRITES, durableWrites);
        specification.with(KeyspaceOption.REPLICATION, toOptions());
        return specification;
    }

    public DropKeyspaceSpecification drop() {
        return DropKeyspaceSpecification.dropKeyspace(keyspaceName);
    }

    private Map<Option, Object> toOptions() {
        MapBuilder<Option, Object> builder = mapBuilder();
        builder.entry(replicationStrategyOption(), replicationStrategy.getValue());
        if (replicationStrategy == ReplicationStrategy.SIMPLE_STRATEGY)
            return builder.entry(replicationFactorOption(), this.replicationFactor).build();
        if (replicationStrategy == ReplicationStrategy.NETWORK_TOPOLOGY_STRATEGY)
            replications.forEach(dcr -> {
                builder.entry(replicationFactorOption(dcr.getDataCenter()), dcr.getReplicationFactor());
            });
        return builder.build();
    }

    private static MapBuilder<Option, Object> mapBuilder() {
        return MapBuilder.map(Option.class, Object.class);
    }

    private static DefaultOption replicationStrategyOption() {
        return new DefaultOption("class", String.class, true, false, true);
    }

    private static DefaultOption replicationFactorOption() {
        return replicationFactorOption("replication_factor");
    }

    private static DefaultOption replicationFactorOption(String name) {
        return new DefaultOption(name, Long.class, true, false, false);
    }
}
