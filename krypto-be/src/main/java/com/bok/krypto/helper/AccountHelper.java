package com.bok.krypto.helper;

import com.bok.krypto.grpc.client.BankGprcClient;
import com.bok.krypto.grpc.client.ParentGrpcClient;
import com.bok.krypto.model.Account;
import com.bok.krypto.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccountHelper {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ParentGrpcClient parentGrpcClient;

    @Autowired
    BankGprcClient bankGprcClient;

    public boolean existsById(Long accountId) {
        return accountRepository.existsById(accountId);
    }

    public String getEmailByAccountId(Long accountId) {
        return parentGrpcClient.getEmailByAccountId(accountId);
    }

    private Account saveOrUpdate(Account account) {
        return accountRepository.save(account);
    }

    public Account createAccount(Long accountId) {
        if (accountRepository.existsById(accountId)) {
            throw new RuntimeException("Account with same id Already exists");
        }
        Account a = new Account();
        a.setId(accountId);
        return saveOrUpdate(a);
    }

    public AccountInfoResponse getAccountInfo(Long accountId) {
        return bankGprcClient.getAccountInfo(accountId);
    }

    public Account findById(Long accountId) {
        return accountRepository.findById(accountId).orElse(null);
    }
}
