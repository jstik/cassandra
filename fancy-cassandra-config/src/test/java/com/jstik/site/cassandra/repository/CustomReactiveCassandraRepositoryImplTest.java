package com.jstik.site.cassandra.repository;

import com.jstik.fancy.test.util.cassandra.CassandraCreateDropSchemaRule;
import com.jstik.fancy.test.util.cassandra.EmbeddedCassandraConfig;
import com.jstik.site.cassandra.exception.EntityAlreadyExistsException;
import com.jstik.site.cassandra.test.TestCassandraConfiguration;
import com.jstik.site.cassandra.test.entity.TestTable;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.cassandra.core.ReactiveCassandraOperations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import reactor.test.StepVerifier;

import javax.inject.Inject;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringJUnitWebConfig
@ContextConfiguration(
        classes = {
                EmbeddedCassandraConfig.class, TestCassandraConfiguration.class
        }
)
@TestPropertySource("classpath:embedded-test.properties")
public class CustomReactiveCassandraRepositoryImplTest {

    @Inject
    private ReactiveCassandraOperations operation;

    @Rule
    @Inject
    public CassandraCreateDropSchemaRule createDropSchemaRule;

    @Test
    public void insertIfNotExist() throws Exception {
        CustomReactiveCassandraRepositoryImpl<TestTable, String> repo = new CustomReactiveCassandraRepositoryImpl<>(operation);
        StepVerifier.create(repo.insertIfNotExist(prepareData())).assertNext(Assert::assertTrue).verifyComplete();
    }

    @Test
    public void insertIfNotExistOrThrow() throws Exception {
        CustomReactiveCassandraRepositoryImpl<TestTable, String> repo = new CustomReactiveCassandraRepositoryImpl<>(operation);
        StepVerifier.create(repo.insertIfNotExistOrThrow(prepareData())).assertNext(Assert::assertNotNull).verifyComplete();
        StepVerifier.create(repo.insertIfNotExistOrThrow(prepareData())).expectError(EntityAlreadyExistsException.class).verify();
    }

    @Test
    public void updateIfExist() throws Exception {
        CustomReactiveCassandraRepositoryImpl<TestTable, String> repo = new CustomReactiveCassandraRepositoryImpl<>(operation);
        StepVerifier.create(repo.updateIfExist(prepareData())).assertNext(Assert::assertFalse).verifyComplete();
    }

    @Test
    public void executeBatch() throws Exception {
    }

    private TestTable prepareData() {
        TestTable testTable = new com.jstik.site.cassandra.test.entity.TestTable("key1");
        testTable.setTestData("Some Test");
        return testTable;
    }

}