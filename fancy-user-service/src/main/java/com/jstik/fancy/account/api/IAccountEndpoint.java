package com.jstik.fancy.account.api;

import com.jstik.fancy.account.model.account.ActivateAccountRequiredInfo;
import com.jstik.fancy.account.model.account.CreateAccountRequest;
import com.jstik.fancy.account.model.account.RegisterAccountRequest;
import com.jstik.fancy.account.model.user.NewUserInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public interface IAccountEndpoint {

    @PostMapping(value = "/account", produces = {"application/json" , "text/event-stream"}, consumes = {"application/json"})
    Mono<NewUserInfo> createAccount(@RequestBody CreateAccountRequest accountRequest);

    @PutMapping(value = "/account", produces = {"application/json" , "text/event-stream"}, consumes = {"application/json"})
    Mono<ActivateAccountRequiredInfo> activateAccount(@RequestBody RegisterAccountRequest registerAccount);
}
