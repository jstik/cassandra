package com.jstik.site.cassandra.statements.batch;

import com.datastax.driver.core.RegularStatement;
import org.reactivestreams.Publisher;
import org.springframework.data.cassandra.core.convert.CassandraConverter;

public interface IBatchOperation {

    Publisher<RegularStatement> toStatement(CassandraConverter converter);
}
