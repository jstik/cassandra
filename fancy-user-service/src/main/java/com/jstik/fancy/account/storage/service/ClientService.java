package com.jstik.fancy.account.storage.service;

import com.jstik.fancy.account.storage.dao.repository.cassandra.client.ClientRepository;
import com.jstik.fancy.account.storage.dao.repository.cassandra.client.UsersByClientRepository;
import com.jstik.fancy.account.storage.entity.cassandra.client.UsersByClient;
import com.jstik.fancy.account.storage.entity.cassandra.user.User;
import com.jstik.site.cassandra.statements.EntityAwareBatchStatement;
import reactor.core.publisher.Mono;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Optional;

import static com.jstik.site.cassandra.statements.DMLStatementProducerBuilder.insertProducer;

public class ClientService {

    private final UsersByClientRepository usersByClientRepository;
    private final ClientRepository clientRepository;

    @Inject
    public ClientService(UsersByClientRepository usersByClientRepository, ClientRepository clientRepository) {
        this.usersByClientRepository = usersByClientRepository;
        this.clientRepository = clientRepository;
    }


    public Mono<Boolean> addUserClients(@NotNull User user, Collection<String> clients) {
        if (clients == null || clients.isEmpty())
            return Mono.just(true);

        if (user.getId() == null)
            return Mono.error(new IllegalStateException("Id must be not null!"));

        EntityAwareBatchStatement batchStatement = insertUsersByClientStatement(user, clients).orElse(null);

        return usersByClientRepository.executeBatch(batchStatement);
    }

    public Mono<Boolean> deleteUserClients(@NotNull User user, Collection<String> clients) {
        if (clients == null || clients.isEmpty())
            return Mono.just(true);

        if (user.getId() == null)
            return Mono.error(new IllegalStateException("Id must be not null!"));
        return usersByClientRepository.deleteUsersByClient(user.getLogin(), clients);
    }

    public Optional<EntityAwareBatchStatement> insertUsersByClientStatement(@NotNull User user, Collection<String> clients) {
        if (clients == null)
            return Optional.empty();
        return clients.stream()
                .map(client -> new EntityAwareBatchStatement(insertProducer(), new UsersByClient(client, user.getLogin())))
                .reduce(EntityAwareBatchStatement::andThen);
    }
}
