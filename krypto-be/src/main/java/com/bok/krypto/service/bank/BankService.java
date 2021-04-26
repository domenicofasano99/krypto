package com.bok.krypto.service.bank;

import com.bok.integration.DepositRequest;
import com.bok.integration.DepositResponse;
import com.bok.integration.AccountBalance;
import com.bok.integration.WithdrawalRequest;
import com.bok.integration.WithdrawalResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BankService {

    @Autowired
    BankClient bankClient;

    public AccountBalance getAccountBalance(Long userId) {
        return bankClient.getBalance(userId);
    }

    public WithdrawalResponse withdraw(Long userId, WithdrawalRequest request) {
        return bankClient.withdraw(userId, request);
    }

    public DepositResponse deposit(Long userId, DepositRequest request) {
        return bankClient.deposit(userId, request);
    }
}
