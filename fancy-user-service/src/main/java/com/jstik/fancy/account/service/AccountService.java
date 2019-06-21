package com.jstik.fancy.account.service;

import com.jstik.fancy.account.dao.repository.*;
import com.jstik.fancy.account.entity.user.User;
import com.jstik.fancy.account.entity.user.UserRegistration;
import com.jstik.fancy.account.entity.user.UserRegistration.UserRegistrationPrimaryKey;
import com.jstik.fancy.account.exception.UserRegistrationNoFound;
import com.jstik.fancy.account.model.account.CreateAccountRequest;
import com.jstik.fancy.account.model.account.NewUserInfo;
import com.jstik.fancy.account.model.account.RegisterAccountRequest;
import com.jstik.fancy.account.model.account.ActivateAccountRequiredInfo;
import org.springframework.core.convert.ConversionService;
import reactor.core.publisher.Mono;

import javax.inject.Inject;


public class AccountService {

    private final ConversionService conversionService;

    private final UserService userService;

    private final UserRegistrationService registrationService;


    @Inject
    public AccountService(ConversionService conversionService, UserService userService, UserRegistrationService registrationService) {
        this.conversionService = conversionService;
        this.userService = userService;
        this.registrationService = registrationService;
    }

    public Mono<NewUserInfo> createAccount(CreateAccountRequest account, String regKey) {
        User user = conversionService.convert(account, User.class);
        return userService.createUser(user, regKey);
    }

    public Mono<ActivateAccountRequiredInfo> activateAccount(RegisterAccountRequest registerAccount) {
        Mono<ActivateAccountRequiredInfo> registrationInformation = findRequiredRegistrationInformation(registerAccount);
        registrationInformation.doOnSuccess((info -> {
            userService.activateUser(info.getUser(), info.getPlainPassword())
                    .delayUntil(user -> {
                        UserRegistration registration = info.getUserRegistration();
                        return registrationService.delete(registration.getLogin(), registration.getRegKey());
                    })
                    .subscribe();
        }));
        return registrationInformation;
    }


    private Mono<ActivateAccountRequiredInfo> findRequiredRegistrationInformation(RegisterAccountRequest request) {
        Mono<UserRegistration> findRegistrationOperation = registrationService.findRegistration(request.getLogin(), request.getRegKey())
                .doOnSuccess(registration -> {
                    if (registration == null)
                        throw new UserRegistrationNoFound();
                });

        Mono<User> findUserOperation = userService.findUserOrThrow(request.getLogin());
        return Mono.zip(findRegistrationOperation, findUserOperation, (reg, user) -> new ActivateAccountRequiredInfo(user, reg, request.getPassword()));
    }
}
