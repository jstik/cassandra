package com.jstik.site.cassandra.config.keyspace;

import org.springframework.data.cassandra.core.cql.keyspace.*;
import org.springframework.data.cassandra.core.cql.keyspace.KeyspaceOption.ReplicationStrategy;

import java.util.ArrayList;
import java.util.List;

public class KeyspaceSpecificationBuilder {

    private List<DataCenterReplication> replications = new ArrayList<>();
    private ReplicationStrategy replicationStrategy = ReplicationStrategy.SIMPLE_STRATEGY;;
    private long replicationFactor;
    private boolean durableWrites;
    private boolean ifNotExists;
    private String keyspaceName;

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

    public KeyspaceSpecificationBuilder ifNotExists(boolean ifNotExists){
        this.ifNotExists = ifNotExists;
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
        return new KeyspaceSpecificationFactory(replications, replicationStrategy, replicationFactor, durableWrites, ifNotExists, keyspaceName );
    }
}
