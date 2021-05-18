package com.bok.krypto.service.bank;

import com.bok.bank.integration.dto.AuthorizationRequestDTO;
import com.bok.bank.integration.dto.AuthorizationResponseDTO;
import com.bok.bank.integration.dto.BankAccountInfoDTO;
import com.bok.bank.integration.message.BankDepositMessage;
import com.bok.bank.integration.message.BankWithdrawalMessage;
import com.bok.bank.integration.util.AuthorizationException;
import com.bok.bank.integration.util.Money;
import com.bok.krypto.service.interfaces.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class BankService {

    @Autowired
    BankClient bankClient;

    @Autowired
    MessageService messageService;

    public AuthorizationResponseDTO authorize(Long accountId, UUID publicTransactionId, Money money, String fromMarket) {
        AuthorizationRequestDTO paymentAmountRequestDTO = new AuthorizationRequestDTO(accountId, publicTransactionId, money, fromMarket);
        try {
            return bankClient.authorize(accountId, paymentAmountRequestDTO);
        } catch (Exception e) {
            log.error("Error while authorizing transaction");
            throw new AuthorizationException("Error while authorizing transaction, try again");
        }

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
