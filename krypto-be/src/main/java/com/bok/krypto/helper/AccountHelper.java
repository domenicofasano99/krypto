package com.bok.krypto.helper;

import com.bok.bank.integration.grpc.AccountInfoResponse;
import com.bok.krypto.model.Account;
import com.bok.krypto.repository.AccountRepository;
import com.bok.krypto.service.bank.BankService;
import com.bok.krypto.service.parent.ParentService;
import com.bok.parent.integration.message.AccountClosureMessage;
import com.bok.parent.integration.message.AccountCreationMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static java.util.Optional.ofNullable;

@Slf4j
@Component
public class AccountHelper {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ParentService parentService;

    @Autowired
    BankService bankService;

    public boolean existsById(Long accountId) {
        return accountRepository.existsByIdAndDeletedIsFalse(accountId);
    }

    public String getEmailByAccountId(Long accountId) {
        String email = parentService.getEmailByAccountId(accountId);
        log.info("Email for accountID {} : {}", accountId, email);
        return ofNullable(email).orElseThrow(() -> new RuntimeException("Error while retrieving account " + accountId + "'s email"));
    }

    private Account saveOrUpdate(Account account) {
        return accountRepository.save(account);
    }

    public AccountInfoResponse getAccountInfo(Long accountId) {
        return bankService.getAccountInfo(accountId);
    }

    public Account findById(Long accountId) {
        return accountRepository.findByIdAndDeletedIsFalse(accountId).orElseThrow(() -> new RuntimeException("Account: " + accountId + " not found."));
    }

    public void createAccount(AccountCreationMessage message) {
        if (accountRepository.existsByIdAndDeletedIsFalse(message.accountId)) {
            throw new RuntimeException("Account with same id Already exists");
        }
        Account a = new Account();
        a.setId(message.accountId);
        saveOrUpdate(a);
    }

    public void closeAccount(AccountClosureMessage message) {
        Account account = accountRepository.findById(message.accountId).orElseThrow(() -> new RuntimeException("Account not found"));
        account.setDeleted(true);
        saveOrUpdate(account);
    }
}
