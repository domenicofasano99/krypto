package com.bok.krypto.service.bank;

import com.bok.bank.integration.dto.CheckPaymentAmountRequestDTO;
import com.bok.bank.integration.dto.CheckPaymentAmountResponseDTO;
import com.bok.krypto.integration.internal.dto.BankAccountDetails;
import com.bok.krypto.integration.internal.dto.DepositRequest;
import com.bok.krypto.integration.internal.dto.DepositResponse;
import com.bok.krypto.integration.internal.dto.WithdrawalRequest;
import com.bok.krypto.integration.internal.dto.WithdrawalResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Currency;

@Service
public class BankService {

    @Autowired
    BankClient bankClient;

    public Boolean preauthorize(Long accountId, Currency currency, BigDecimal amount) {
        CheckPaymentAmountRequestDTO paymentAmountRequestDTO = new CheckPaymentAmountRequestDTO(Currency.getInstance("USD"), amount);
        CheckPaymentAmountResponseDTO response = bankClient.checkPaymentAmount(accountId, paymentAmountRequestDTO);
        return response.available;
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
