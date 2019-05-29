package com.jstik.site.cassandra.statements.batch;

import com.datastax.driver.core.RegularStatement;
import org.reactivestreams.Publisher;
import org.springframework.data.cassandra.core.convert.CassandraConverter;
import reactor.core.publisher.Mono;

import java.util.function.BiFunction;

public class BatchOperation implements IBatchOperation {

     private BiFunction<CassandraConverter, Object, RegularStatement> statementFunction;

     private Object entity;

    public BatchOperation(BiFunction<CassandraConverter, Object, RegularStatement> statementFunction, Object entity) {
        this.statementFunction = statementFunction;
        this.entity = entity;
    }


    public Publisher<RegularStatement> toStatement(CassandraConverter converter){
        return Mono.fromSupplier(() -> statementFunction.apply(converter, entity));
    }
}
