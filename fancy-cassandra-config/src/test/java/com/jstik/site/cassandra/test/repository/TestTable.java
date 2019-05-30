package com.jstik.site.cassandra.test.repository;

import com.jstik.site.cassandra.repository.CustomReactiveCassandraRepository;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;

public interface TestTable extends ReactiveCassandraRepository<TestTable, String>, CustomReactiveCassandraRepository<TestTable, String> {
}
