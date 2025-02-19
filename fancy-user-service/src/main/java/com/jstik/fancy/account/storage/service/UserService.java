package com.jstik.fancy.account.storage.service;

import com.jstik.fancy.account.handler.operation.DMLOperationHandler;
import com.jstik.fancy.account.model.exception.UserNotFound;
import com.jstik.fancy.account.model.user.IUser;
import com.jstik.fancy.account.model.user.NewUserInfo;
import com.jstik.fancy.account.storage.dao.repository.cassandra.user.UserRepository;
import com.jstik.fancy.account.storage.entity.cassandra.user.User;
import com.jstik.fancy.account.storage.entity.cassandra.user.UserRegistration;
import com.jstik.site.cassandra.statements.EntityAwareBatchStatement;
import org.springframework.beans.factory.ObjectProvider;
import reactor.core.publisher.Mono;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static reactor.core.publisher.Mono.error;

public class UserService implements IUserService {

    private final UserRepository userRepository;
    private ClientService clientService;
    private AuthorityService authorityService;
    private ObjectProvider<Collection<DMLOperationHandler<IUser>>> dmlHandlers;
    private TagService tagService;


    @Inject
    public UserService(UserRepository userRepository,
                       TagService tagService,
                       ClientService clientService,
                       AuthorityService authorityService,
                       ObjectProvider<Collection<DMLOperationHandler<IUser>>> dmlHandlers
    ) {
        this.userRepository = userRepository;
        this.tagService = tagService;
        this.clientService = clientService;
        this.authorityService = authorityService;
        this.dmlHandlers = dmlHandlers;
    }

    @Override
    public Mono<NewUserInfo> createUser(User user, Mono<UserRegistration> registration) {
        Mono<NewUserInfo> result  = Mono.zip(insertUser(user), registration, (inserted, reg)->{
            NewUserInfo info =  new NewUserInfo();
            info.setRegKey(reg.getRegKey());
            info.setUser(inserted);
            return info;
        });
        return result.doOnSuccess(info -> {
            insertBrandNewUserLinkedInBatch(info.getUser()).subscribe();
            if (info.getUser().getTags() != null && !info.getUser().getTags().isEmpty()) {
                tagService.addTagsForEntity(info.getUser().getTags(), info.getUser()).subscribe();
            }
        });
    }

    @Override
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

    @Override
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

    @Override
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

    @Override
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


    @Override
    public Mono<User> activateUser(User user, String password) {
        user.setActive(true);
        user.setPassword(password);
        return updateUser(user);
    }

    @Override
    public Mono<User> updateUser(User user) {
        DMLOperationHandler<IUser> composite = chainHandlers();
        Mono<User> userMono = userRepository.save(user);
        return composite.handleUpdate(userMono);
    }

    @Override
    public Mono<User> addUserGroups(Collection<String> groups, User user) {
        if (groups == null)
            return Mono.just(user);
        Set<String> filtered = groups.stream().filter(Objects::nonNull).collect(Collectors.toSet());
        if (filtered.isEmpty())
            return Mono.just(user);
        user.addGroups(filtered);
        return updateUser(user);
    }

    @Override
    public Mono<User> deleteUserGroups(Collection<String> groups, User user) {
        if (groups == null)
            return Mono.just(user);
        Set<String> filtered = groups.stream().filter(Objects::nonNull).collect(Collectors.toSet());
        if (filtered.isEmpty())
            return Mono.just(user);
        user.deleteGroups(filtered);
        return updateUser(user);
    }

    Mono<User> insertUser(User user) {
        DMLOperationHandler<IUser> composite = chainHandlers();
        Mono<User> userMono = userRepository.insertIfNotExistOrThrow(user);
        return composite.handleInsert(userMono);
    }

    private DMLOperationHandler<IUser> chainHandlers() {
        Collection<DMLOperationHandler<IUser>> handlers = this.dmlHandlers.getIfAvailable(ArrayList::new);
        return handlers.stream().reduce(DMLOperationHandler::andThen).orElse(new DMLOperationHandler<IUser>(){
            @Override
            public <S extends IUser> Mono<S> handleInsert(Mono<S> userMono) {
                return userMono;
            }

            @Override
            public <S extends IUser> Mono<S> handleUpdate(Mono<S> userMono) {
                return userMono;
            }
        });
    }

    @Override
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
