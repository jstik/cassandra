package com.jstik.site.cassandra.config.setup.keyspace;

import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.data.cassandra.config.KeyspaceAction;
import org.springframework.data.cassandra.core.cql.keyspace.*;
import org.springframework.data.cassandra.core.cql.keyspace.KeyspaceOption.ReplicationStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class KeyspaceSpecificationBuilder {

    private List<DataCenterReplication> replications = new ArrayList<>();
    private ReplicationStrategy replicationStrategy = ReplicationStrategy.SIMPLE_STRATEGY;;
    private long replicationFactor;
    private boolean durableWrites;
    private boolean ifNotExists;
    private String keyspaceName;
    private KeyspaceAction keyspaceAction;

    private KeyspaceSpecificationBuilder(String keyspaceName) {
        this.keyspaceName = keyspaceName;
    }

    public static KeyspaceSpecificationBuilder from(String keyspaceName){
      return new KeyspaceSpecificationBuilder(keyspaceName);
    }

    public KeyspaceSpecificationBuilder ifNotExists(){
        this.ifNotExists = true;
        return this;
    }

    public KeyspaceSpecificationBuilder durableWrites(){
        durableWrites = true;
        return this;
    }

    public KeyspaceSpecificationBuilder withSimpleReplication(long replicationFactor){
        this.replicationFactor = replicationFactor;
        return this;
    }

    public KeyspaceSpecificationBuilder withNetworkReplication(List<DataCenterReplication> replications){
        this.replications = replications;
        this.replicationStrategy = ReplicationStrategy.NETWORK_TOPOLOGY_STRATEGY;
        return this;
    }

    public KeyspaceSpecificationFactory build(){
        return new KeyspaceSpecificationFactory(replications, replicationStrategy, replicationFactor, durableWrites, ifNotExists, keyspaceName, keyspaceAction );
    }

    public KeyspaceSpecificationBuilder keyspaceAction(KeyspaceAction keyspaceAction){
        this.keyspaceAction = keyspaceAction;
        return this;
    }

    public KeyspaceSpecificationFactory build(KeyspaceProperties properties){
        PropertyMapper mapper = PropertyMapper.get();
        mapper.from(properties.getKeyspaceAction()).whenNonNull().to(this::keyspaceAction);
        mapper.from(properties.isIfNotExists()).whenTrue().to(val->ifNotExists());
        mapper.from(properties.isDurableWrites()).whenTrue().to(val->durableWrites());
        mapper.from(properties.getReplicationStrategy())
                .whenEqualTo(ReplicationStrategy.NETWORK_TOPOLOGY_STRATEGY)
                .to(val->{
                    List<DataCenterReplication> replications = properties.dataCenterReplication().stream()
                            .map(pr -> DataCenterReplication.of(pr.getDataCenter(), pr.getReplicationFactor()))
                            .collect(Collectors.toList());
                    withNetworkReplication(replications);
                });
        return build();
    }
}
