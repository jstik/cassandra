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

    private final UserRegistrationRepository userRegistrationRepository;


    @Inject
    public AccountService(ConversionService conversionService, UserService userService, UserRegistrationRepository userRegistrationRepository) {
        this.conversionService = conversionService;
        this.userService = userService;
        this.userRegistrationRepository = userRegistrationRepository;
    }

    public Mono<NewUserInfo> createAccount(CreateAccountRequest account, String regKey) {
        User user = conversionService.convert(account, User.class);
        return userService.createUser(user, regKey);
    }

    public Mono<ActivateAccountRequiredInfo> activateAccount(RegisterAccountRequest registerAccount) {
        Mono<ActivateAccountRequiredInfo> registrationInformation = findRequiredRegistrationInformation(registerAccount);
        registrationInformation.doOnSuccess((info -> doRegisterUser(info.getUserRegistration(), info.getUser()).subscribe()));
        return registrationInformation;
    }


    private Mono<ActivateAccountRequiredInfo> findRequiredRegistrationInformation(RegisterAccountRequest registerAccount) {
        UserRegistrationPrimaryKey registrationId = new UserRegistrationPrimaryKey(registerAccount.getLogin(), registerAccount.getRegKey());
        Mono<UserRegistration> findRegistrationOperation = userRegistrationRepository.findById(registrationId)
                .doOnSuccess(registration -> {
                    if (registration == null)
                        throw new UserRegistrationNoFound();
                });

        Mono<User> findUserOperation = userService.getUserToActivate(registerAccount.getLogin(), registerAccount.getPassword());
        return Mono.zip(findRegistrationOperation, findUserOperation, (reg, user) -> new ActivateAccountRequiredInfo(user, reg));
    }


    private Mono<User> doRegisterUser(UserRegistration userRegistration, User user) {
        return userService.saveUserAndDelayUntil(user, userRegistrationRepository.delete(userRegistration));
    }
}
