package com.bok.krypto.helper;

import com.bok.krypto.model.Account;
import com.bok.krypto.model.Wallet;
import com.bok.krypto.repository.AccountRepository;
import com.bok.integration.AccountCreationMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccountHelper {

    @Autowired
    AccountRepository accountRepository;

    public boolean existsById(Long accountId) {
        return accountRepository.existsById(accountId);
    }

    public Account findById(Long accountId) {
        return accountRepository.findById(accountId).orElse(null);
    }

    public String findEmailByAccountId(Long accountId) {
        return accountRepository.findEmailByAccountId(accountId);
    }

    public Account saveOrUpdate(Account account) {
        return accountRepository.save(account);
    }

    public void handle(AccountCreationMessage message) {
        Account a = new Account();
        a.setId(message.accountId);
        a.setEmail(message.email);
        saveOrUpdate(a);
    }

    public Boolean checkRightsOnResource(Account account, Wallet wallet) {
        return wallet.getAccount().equals(account);
    }
}
