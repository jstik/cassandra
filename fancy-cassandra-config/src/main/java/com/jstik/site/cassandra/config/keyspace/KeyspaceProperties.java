package com.jstik.site.cassandra.config.keyspace;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.cassandra.config.KeyspaceAction;
import org.springframework.data.cassandra.core.cql.keyspace.KeyspaceOption.ReplicationStrategy;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@Getter
@Setter
public class KeyspaceProperties {

    private ReplicationStrategy replicationStrategy = ReplicationStrategy.SIMPLE_STRATEGY;
    private long replicationFactor;
    private boolean durableWrites;
    private boolean ifNotExists;
    private KeyspaceAction keyspaceAction = KeyspaceAction.NONE;

    private Map<String, Long> dataCenterReplication = new LinkedHashMap<>();


    public  List<DataCenterReplicationProperties> dataCenterReplication(){
       return dataCenterReplication.entrySet().stream().map(entry-> new DataCenterReplicationProperties(entry.getKey(), entry.getValue())).collect(toList());
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DataCenterReplicationProperties{
        private String dataCenter;
        private long replicationFactor;
    }
}
