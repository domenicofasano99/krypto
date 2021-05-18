package com.bok.krypto.service.bank;

import com.bok.bank.integration.dto.AuthorizationResponseDTO;
import com.bok.bank.integration.dto.AuthorizationRequestDTO;
import com.bok.bank.integration.dto.BankAccountInfoDTO;
import com.bok.bank.integration.service.BankAccountController;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name = "BankClient", url = "http://bank:8080/")
public interface BankClient extends BankAccountController {

    @Override
    @PostMapping("/bankAccount/authorize")
    AuthorizationResponseDTO authorize(@RequestParam("accountId") Long accountId, @RequestBody AuthorizationRequestDTO authorizationRequestDTO);

    @Override
    @GetMapping("/bankAccount/bankAccountInfo")
    BankAccountInfoDTO bankAccountInfo(@RequestParam("accountId") Long accountId);

}