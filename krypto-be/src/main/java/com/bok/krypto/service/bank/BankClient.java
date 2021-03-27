package com.bok.krypto.service.bank;

import com.bok.integration.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@FeignClient(name = "BankClient", url = "http://bank:8080/")
public interface BankClient {

    @GetMapping(value = "/{userId}/balance", consumes = MediaType.APPLICATION_JSON_VALUE)
    UserBalance getBalance(@PathVariable("userId") Long userId);

    @PostMapping(value = "/{userId}/withdraw")
    WithdrawalResponse withdraw(@PathVariable("userId") Long userId, WithdrawalRequest request);

    @PostMapping(value = "/{userId}/deposit")
    DepositResponse deposit(@PathVariable("userId") Long userId, DepositRequest request);

}