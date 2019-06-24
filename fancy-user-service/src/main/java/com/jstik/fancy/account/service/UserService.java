package com.jstik.fancy.account.service;

import com.jstik.fancy.account.dao.repository.UserOperationsRepository;
import com.jstik.fancy.account.dao.repository.UserRepository;
import com.jstik.fancy.account.entity.user.User;
import com.jstik.fancy.account.entity.user.UserOperations;
import com.jstik.fancy.account.entity.user.UserRegistration;
import com.jstik.fancy.account.exception.UserNotFound;
import com.jstik.fancy.account.model.account.NewUserInfo;
import com.jstik.site.cassandra.statements.EntityAwareBatchStatement;
import reactor.core.publisher.Mono;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.jstik.site.cassandra.model.EntityOperation.CREATE;
import static com.jstik.site.cassandra.model.EntityOperation.UPDATE;
import static reactor.core.publisher.Mono.error;

public class UserService {

    private final UserRepository userRepository;
    private ClientService clientService;
    private AuthorityService authorityService;
    private UserOperationsRepository operationsRepository;
    private TagService tagService;


    @Inject
    public UserService(UserRepository userRepository,
                       UserOperationsRepository operationsRepository,
                       TagService tagService,
                       ClientService clientService,
                       AuthorityService authorityService
    ) {
        this.userRepository = userRepository;
        this.operationsRepository = operationsRepository;
        this.tagService = tagService;
        this.clientService = clientService;
        this.authorityService = authorityService;
    }

    public Mono<NewUserInfo> createUser(User user, Mono<UserRegistration> registration) {
        Mono<NewUserInfo> result = Mono.just(new NewUserInfo());

        result = result.delayUntil(info -> insertUser(user).doOnSuccess(inserted -> info.setUser(user)));

        result = result.delayUntil(info -> registration.handle((reg, silk) -> {
            info.setRegKey(reg.getRegKey());
            silk.next(reg);
        }));

        return result.doOnSuccess(info -> {
            insertBrandNewUserLinkedInBatch(info.getUser()).doOnSuccess(info::setLinkedInserted).subscribe();
            if (info.getUser().getTags() != null && !info.getUser().getTags().isEmpty()) {
                tagService.addTagsForEntity(info.getUser().getTags(), info.getUser()).subscribe();
            }
        });
    }

    public Mono<User> addUserTags(Collection<String> tags, User user) {
        if (tags == null)
            return Mono.just(user);
        Set<String> filtered = tags.stream().filter(Objects::nonNull).collect(Collectors.toSet());
        if (filtered.isEmpty())
            return Mono.just(user);
        user.addTags(filtered);
        return updateUser(user).doOnSuccess(updated -> {
            tagService.addTagsForEntity(filtered, user).subscribe();
        });
    }

    public Mono<User> deleteUserTags(Collection<String> tags, User user) {
        if (tags == null)
            return Mono.just(user);
        Set<String> filtered = tags.stream().filter(Objects::nonNull).collect(Collectors.toSet());
        if (filtered.isEmpty())
            return Mono.just(user);
        user.deleteTags(filtered);
        return updateUser(user).doOnSuccess(updated -> {
            tagService.deleteTagsForEntity(filtered, user).subscribe();
        });
    }

    public Mono<User> addUserClients(Collection<String> clients, User user) {
        if (clients == null)
            return Mono.just(user);
        Set<String> filtered = clients.stream().filter(Objects::nonNull).collect(Collectors.toSet());
        if (filtered.isEmpty())
            return Mono.just(user);
        user.addClients(filtered);
        return updateUser(user).doOnSuccess(updated -> {
            clientService.addUserClients(user, clients).subscribe();
        });
    }

    public Mono<User> deleteUserClients(Collection<String> clients, User user) {
        if (clients == null)
            return Mono.just(user);
        Set<String> filtered = clients.stream().filter(Objects::nonNull).collect(Collectors.toSet());
        if (filtered.isEmpty())
            return Mono.just(user);
        user.deleteClients(filtered);
        return updateUser(user).doOnSuccess(updated -> {
            clientService.deleteUserClients(user, filtered).subscribe();
        });
    }


    public Mono<User> activateUser(User user, String password) {
        user.setActive(true);
        user.setPassword(password);
        return updateUser(user);
    }

    Mono<User> updateUser(User user) {
        return userRepository.save(user).doOnSuccess(updated -> {
            UserOperations operations = new UserOperations(updated, UPDATE);
            operationsRepository.save(operations).subscribe();
        });
    }

    Mono<User> insertUser(User user) {
        return userRepository.insertIfNotExistOrThrow(user).doOnSuccess(created -> {
            UserOperations operation = new UserOperations(created, CREATE);
            operationsRepository.save(operation).subscribe();
        });
    }

    public Mono<User> findUserOrThrow(String login) {
        return userRepository.findByPrimaryKeyLogin(login).switchIfEmpty(error(new UserNotFound()));
    }


    Mono<Boolean> insertBrandNewUserLinkedInBatch(User user) {
        EntityAwareBatchStatement batch = Stream.of(
                authorityService.insertAuthority(user.getAuthorities()).orElse(null),
                clientService.insertUsersByClientStatement(user, user.getClients()).orElse(null)
        ).filter(Objects::nonNull).reduce(EntityAwareBatchStatement::andThen).orElse(null);
        return batch != null ? userRepository.executeBatch(batch) : Mono.just(true);
    }
}
