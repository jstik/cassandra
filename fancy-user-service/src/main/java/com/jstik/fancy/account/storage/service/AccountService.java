package com.jstik.fancy.account.storage.service;

import com.jstik.fancy.account.storage.entity.cassandra.user.User;
import com.jstik.fancy.account.storage.entity.cassandra.user.UserRegistration;
import com.jstik.fancy.account.model.account.ActivateAccountRequiredInfo;
import com.jstik.fancy.account.model.account.CreateAccountRequest;
import com.jstik.fancy.account.model.user.NewUserInfo;
import com.jstik.fancy.account.model.account.RegisterAccountRequest;
import com.jstik.fancy.account.util.UserUtil;
import org.springframework.core.convert.ConversionService;
import reactor.core.publisher.Mono;

import javax.inject.Inject;


public class AccountService implements IAccountService {

    private final ConversionService conversionService;

    private final IUserService IUserService;

    private final UserRegistrationService registrationService;


    @Inject
    public AccountService(ConversionService conversionService, IUserService IUserService, UserRegistrationService registrationService) {
        this.conversionService = conversionService;
        this.IUserService = IUserService;
        this.registrationService = registrationService;
    }

    @Override
    public Mono<NewUserInfo> createAccount(CreateAccountRequest account) {
        User user = conversionService.convert(account, User.class);
        String regKey = UserUtil.generateRegKey();
        Mono<UserRegistration> registration = registrationService.createRegistration(user.getLogin(), regKey);

        return IUserService.createUser(user, registration);
    }


    @Override
    public Mono<ActivateAccountRequiredInfo> activateAccount(RegisterAccountRequest registerAccount) {
        Mono<ActivateAccountRequiredInfo> registrationInformation = findRequiredRegistrationInformation(registerAccount);
        return registrationInformation.doOnSuccess((info -> {
            IUserService.activateUser(info.getUser(), registrationService.encodePassword(info.getPlainPassword()))
                    .delayUntil(user -> {
                        UserRegistration registration = info.getUserRegistration();
                        return registrationService.delete(registration.getLogin(), registration.getRegKey());
                    }).subscribe();
        }));
    }


    private Mono<ActivateAccountRequiredInfo> findRequiredRegistrationInformation(RegisterAccountRequest request) {
        Mono<UserRegistration> findRegistrationOperation = registrationService.findRegistrationOrThrow(request.getLogin(), request.getRegKey());
        Mono<User> findUserOperation = IUserService.findUserOrThrow(request.getLogin());
        return Mono.zip(findRegistrationOperation, findUserOperation, (reg, user) -> new ActivateAccountRequiredInfo(user, reg, request.getPassword()));
    }
}
