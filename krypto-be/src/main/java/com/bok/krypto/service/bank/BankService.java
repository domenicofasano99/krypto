package com.bok.krypto.service.bank;

import com.bok.krypto.integration.internal.dto.BankAccountBalance;
import com.bok.krypto.integration.internal.dto.BankAccountDetails;
import com.bok.krypto.integration.internal.dto.DepositRequest;
import com.bok.krypto.integration.internal.dto.DepositResponse;
import com.bok.krypto.integration.internal.dto.WithdrawalRequest;
import com.bok.krypto.integration.internal.dto.WithdrawalResponse;
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
