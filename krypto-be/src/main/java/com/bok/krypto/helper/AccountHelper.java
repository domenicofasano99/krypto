package com.bok.krypto.helper;

import com.bok.krypto.grpc.client.ParentGrpcClient;
import com.bok.krypto.model.Account;
import com.bok.krypto.model.Wallet;
import com.bok.krypto.repository.AccountRepository;
import com.bok.parent.integration.message.AccountCreationMessage;
import com.bok.parent.integration.message.AccountDeletionMessage;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class AccountHelper {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ParentGrpcClient parentGrpcClient;

    public boolean existsById(Long accountId) {
        return accountRepository.existsById(accountId);
    }

    public Account findById(Long accountId) {
        return accountRepository.findById(accountId).orElse(null);
    }

    public String findEmailByAccountId(Long accountId) {
        return parentGrpcClient.getEmailByAccountId(accountId);
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

    public void handle(AccountDeletionMessage message) {
        Account a = findById(message.accountId);
        accountRepository.delete(a);
    }

    public void createAccount(Long accountId, String email) {
        Account a = new Account();
        a.setId(accountId);
        a.setEmail(email);
        saveOrUpdate(a);
    }

    //TODO complete
    public AccountInfo getAccountInfo(Long accountId) {
        return null;
    }

    @Data
    public static class AccountInfo {
        public String name;
        public String middleName;
        public String surname;
        public LocalDate birthDay;
        public String email;
    }
}
