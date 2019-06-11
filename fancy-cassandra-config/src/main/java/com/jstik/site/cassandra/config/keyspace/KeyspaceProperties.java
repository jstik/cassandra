package com.jstik.site.cassandra.config.keyspace;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KeyspaceProperties {

    private String replicationStrategy;
    private long replicationFactor;
    private boolean durableWrites;
    private boolean ifNotExists;
    private String keyspaceName;
    private DataCenterReplicationProperties dataCenterReplication;

    @Getter
    @Setter
    public static class DataCenterReplicationProperties{
        private String dataCenter;
        private long replicationFactor;
    }
}
