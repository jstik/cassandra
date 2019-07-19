package com.jstik.fancy.account.api;

import com.jstik.fancy.account.model.account.ActivateAccountRequiredInfo;
import com.jstik.fancy.account.model.account.CreateAccountRequest;
import com.jstik.fancy.account.model.account.RegisterAccountRequest;
import com.jstik.fancy.account.model.user.NewUserInfo;
import com.jstik.fancy.account.storage.service.IAccountService;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.inject.Inject;

@RestController
public class AccountEndpoint implements IAccountEndpoint {

    private final IAccountService accountService;

    @Inject
    public AccountEndpoint(IAccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public Mono<NewUserInfo> createAccount(CreateAccountRequest accountRequest){
        return accountService.createAccount(accountRequest);
    }

    @Override
    public Mono<ActivateAccountRequiredInfo> activateAccount(RegisterAccountRequest registerAccount){
        return  accountService.activateAccount(registerAccount);
    }

}
