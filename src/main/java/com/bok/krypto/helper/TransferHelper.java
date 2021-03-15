package com.bok.krypto.helper;

import com.bok.integration.EmailMessage;
import com.bok.integration.TransfersInfoDTO;
import com.bok.integration.TransfersInfoRequestDTO;
import com.bok.integration.krypto.dto.TransferInfoDTO;
import com.bok.integration.krypto.dto.TransferInfoRequestDTO;
import com.bok.integration.krypto.dto.TransferRequestDTO;
import com.bok.integration.krypto.dto.TransferResponseDTO;
import com.bok.krypto.exception.InsufficientBalanceException;
import com.bok.krypto.exception.TransactionNotFoundException;
import com.bok.krypto.messaging.TransferMessage;
import com.bok.krypto.model.Transaction;
import com.bok.krypto.model.Wallet;
import com.bok.krypto.repository.TransactionRepository;
import com.bok.krypto.service.interfaces.MessageService;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;

import static com.bok.krypto.core.Constants.PENDING;

@Component
public class TransferHelper {

    @Autowired
    KryptoHelper kryptoHelper;

    @Autowired
    WalletHelper walletHelper;

    @Autowired
    UserHelper userHelper;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    MessageService messageService;

    public TransferResponseDTO transfer(Long userId, TransferRequestDTO transferRequestDTO) {
        Preconditions.checkArgument(transferRequestDTO.amount.compareTo(BigDecimal.ZERO) < 0);
        Preconditions.checkArgument(userHelper.existsById(userId));
        Preconditions.checkArgument(walletHelper.existsByUserIdAndSymbol(userId, transferRequestDTO.symbol));
        TransferMessage message = new TransferMessage();
        message.userId = userId;
        message.symbol = transferRequestDTO.symbol;
        message.destination = transferRequestDTO.destination;
        message.amount = transferRequestDTO.amount;
        messageService.send(message);

        TransferResponseDTO response = new TransferResponseDTO();
        response.status = PENDING;
        return response;
    }

    public TransferInfoDTO getTransferInfo(TransferInfoRequestDTO transferInfoRequestDTO) {
        Long transferId = transferInfoRequestDTO.transferId;
        Transaction t = transactionRepository.findById(transferId).orElseThrow(() -> new TransactionNotFoundException("Could not find a transaction with the given ID"));
        TransferInfoDTO response = new TransferInfoDTO();
        response.id = t.getId();
        response.type = t.getType().name();
        response.amount = t.getAmount();
        response.source = t.getSourceWallet().getId();
        response.destination = t.getDestinationWallet().getId();
        response.timestamp = t.getTimestamp();
        return response;
    }

    public TransfersInfoDTO getTransfersInfo(TransfersInfoRequestDTO transfersInfoRequestDTO) {
        List<Long> transfersIds = transfersInfoRequestDTO.transferIds;
        if (CollectionUtils.isEmpty(transfersIds)) {
            TransfersInfoDTO emptyDTO = new TransfersInfoDTO();

        }
        List<Transaction> transactions = transactionRepository.findAllById(transfersIds);
        return null;
    }

    public void handle(TransferMessage transferMessage) {

        Wallet source = walletHelper.findByUserIdAndSymbol(transferMessage.userId, transferMessage.symbol);
        Wallet destination = walletHelper.findById(transferMessage.destination);
        try {
            walletHelper.withdraw(source, transferMessage.amount);
        } catch (InsufficientBalanceException ex) {
            String u = userHelper.findEmailByUserId(transferMessage.userId);
            EmailMessage email = new EmailMessage();
            email.subject = "Insufficient Balance in your account";
            email.to = u;
            email.text = "Your transfer of " + transferMessage.amount + " " + transferMessage.symbol + " has been DECLINED due to insufficient balance.";
            messageService.send(email);
        }
        walletHelper.deposit(destination, transferMessage.amount);
        String u = userHelper.findEmailByUserId(transferMessage.userId);
        EmailMessage email = new EmailMessage();
        email.subject = "Transfer executed";
        email.to = u;
        email.text = "Your transfer of " + transferMessage.amount + " " + transferMessage.symbol + " has been ACCEPTED.";
        messageService.send(email);
    }
}
