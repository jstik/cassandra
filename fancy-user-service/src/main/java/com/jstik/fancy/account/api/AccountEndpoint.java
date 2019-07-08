package com.jstik.fancy.account.api;

import com.jstik.fancy.account.model.account.ActivateAccountRequiredInfo;
import com.jstik.fancy.account.model.account.CreateAccountRequest;
import com.jstik.fancy.account.model.account.RegisterAccountRequest;
import com.jstik.fancy.account.model.user.NewUserInfo;
import com.jstik.fancy.account.storage.service.AccountService;
import reactor.core.publisher.Mono;

import javax.inject.Inject;

public class AccountEndpoint {

    @Inject
    private AccountService accountService;

    public Mono<NewUserInfo> createAccount(CreateAccountRequest accountRequest){
        return accountService.createAccount(accountRequest);
    }

    public Mono<ActivateAccountRequiredInfo> activateAccount(RegisterAccountRequest registerAccount){
        return  accountService.activateAccount(registerAccount);
    }

}
