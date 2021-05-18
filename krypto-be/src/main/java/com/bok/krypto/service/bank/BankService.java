package com.bok.krypto.service.bank;

import com.bok.bank.integration.util.Money;
import com.bok.bank.integration.dto.AuthorizationRequestDTO;
import com.bok.bank.integration.dto.AuthorizationResponseDTO;
import com.bok.bank.integration.dto.BankAccountInfoDTO;
import com.bok.bank.integration.message.BankDepositMessage;
import com.bok.bank.integration.message.BankWithdrawalMessage;
import com.bok.krypto.service.interfaces.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BankService {

    @Autowired
    BankClient bankClient;

    @Autowired
    MessageService messageService;

    public AuthorizationResponseDTO preauthorize(Long accountId, Money money, String fromMarket) {
        AuthorizationRequestDTO paymentAmountRequestDTO = new AuthorizationRequestDTO(accountId, money, fromMarket);
        return bankClient.authorize(accountId, paymentAmountRequestDTO);
    }

    public BankAccountInfoDTO getAccountDetails(Long accountId) {
        return bankClient.bankAccountInfo(accountId);
    }

    public void sendBankWithdrawal(BankWithdrawalMessage bankWithdrawalMessage) {
        messageService.sendBankWithdrawal(bankWithdrawalMessage);
    }

    public void sendBankDeposit(BankDepositMessage bankDepositMessage) {
        messageService.sendBankDeposit(bankDepositMessage);
    }
}
