package com.bok.krypto.service.bank;

import com.bok.krypto.integration.internal.dto.BankAccountBalance;
import com.bok.krypto.integration.internal.dto.BankAccountDetails;
import com.bok.krypto.integration.internal.dto.DepositRequest;
import com.bok.krypto.integration.internal.dto.DepositResponse;
import com.bok.krypto.integration.internal.dto.WithdrawalRequest;
import com.bok.krypto.integration.internal.dto.WithdrawalResponse;
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