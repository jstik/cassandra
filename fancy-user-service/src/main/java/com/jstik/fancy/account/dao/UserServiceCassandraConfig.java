package com.jstik.fancy.account.dao;


import com.jstik.fancy.account.entity.cassandra.user.User;
import com.jstik.fancy.account.entity.cassandra.user.User.UserPrimaryKey;
import com.jstik.site.cassandra.config.ReactiveCassandraConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.cassandra.repository.config.EnableReactiveCassandraRepositories;

@Configuration
@Import(ReactiveCassandraConfiguration.class)
@EnableReactiveCassandraRepositories({"com.jstik.fancy.account.dao.repository", "com.jstik.site.cassandra.repository"})
public class UserServiceCassandraConfig {


    /*@Bean
    public CustomUserRepository customUserRepository(ReactiveCassandraOperations operations){
        CassandraMappingContext mappingContext = operations.getConverter().getMappingContext();
        final CassandraPersistentEntity<User> entity = (CassandraPersistentEntity<User>) mappingContext.getRequiredPersistentEntity(User.class);
        final CassandraEntityInformation<User, UserPrimaryKey> metadata = new MappingCassandraEntityInformation<>(entity, operations.getConverter());
        return new CustomUserRepositoryImpl(metadata, operations);
    }*/
}
