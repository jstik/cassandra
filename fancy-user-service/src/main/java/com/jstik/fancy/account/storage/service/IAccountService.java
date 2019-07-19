package com.jstik.fancy.account.storage.service;

import com.jstik.fancy.account.model.account.ActivateAccountRequiredInfo;
import com.jstik.fancy.account.model.account.CreateAccountRequest;
import com.jstik.fancy.account.model.account.RegisterAccountRequest;
import com.jstik.fancy.account.model.user.NewUserInfo;
import reactor.core.publisher.Mono;

public interface IAccountService {
    Mono<NewUserInfo> createAccount(CreateAccountRequest account);

    Mono<ActivateAccountRequiredInfo> activateAccount(RegisterAccountRequest registerAccount);
}
