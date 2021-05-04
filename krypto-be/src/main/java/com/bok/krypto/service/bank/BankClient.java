package com.bok.krypto.service.bank;

import com.bok.integration.BankAccountBalance;
import com.bok.integration.BankAccountDetails;
import com.bok.integration.DepositRequest;
import com.bok.integration.DepositResponse;
import com.bok.integration.WithdrawalRequest;
import com.bok.integration.WithdrawalResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@FeignClient(name = "BankClient", url = "http://bank:8080/")
public interface BankClient {

    @GetMapping(value = "/{accountId}/balance", consumes = MediaType.APPLICATION_JSON_VALUE)
    BankAccountBalance getBalance(@PathVariable("accountId") Long accountId);

    @PostMapping(value = "/{accountId}/withdraw")
    WithdrawalResponse withdraw(@PathVariable("accountId") Long accountId, WithdrawalRequest request);

    @PostMapping(value = "/{accountId}/deposit")
    DepositResponse deposit(@PathVariable("accountId") Long accountId, DepositRequest request);

    @GetMapping(value = "/{accountId}/details", consumes = MediaType.APPLICATION_JSON_VALUE)
    BankAccountDetails getAccountDetails(Long accountId);
}