package com.bok.krypto.service.bank;

import com.bok.integration.BankAccountBalance;
import com.bok.integration.BankAccountDetails;
import com.bok.integration.DepositRequest;
import com.bok.integration.DepositResponse;
import com.bok.integration.WithdrawalRequest;
import com.bok.integration.WithdrawalResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BankService {

    @Autowired
    BankClient bankClient;

    public BankAccountBalance getAccountBalance(Long accountId) {
        return bankClient.getBalance(accountId);
    }

    public WithdrawalResponse withdraw(Long accountId, WithdrawalRequest request) {
        return bankClient.withdraw(accountId, request);
    }

    public DepositResponse deposit(Long accountId, DepositRequest request) {
        return bankClient.deposit(accountId, request);
    }

    public BankAccountDetails getAccountDetails(Long accountId) {
        return bankClient.getAccountDetails(accountId);
    }
}
