package com.bok.krypto.helper;

import com.bok.integration.EmailMessage;
import com.bok.integration.StatusDTO;
import com.bok.integration.TransfersInfoDTO;
import com.bok.integration.TransfersInfoRequestDTO;
import com.bok.integration.krypto.dto.TransferInfoDTO;
import com.bok.integration.krypto.dto.TransferInfoRequestDTO;
import com.bok.integration.krypto.dto.TransferRequestDTO;
import com.bok.integration.krypto.dto.TransferResponseDTO;
import com.bok.krypto.exception.InsufficientBalanceException;
import com.bok.krypto.exception.TransactionNotFoundException;
import com.bok.krypto.messaging.messages.TransferMessage;
import com.bok.krypto.model.*;
import com.bok.krypto.repository.TransferRepository;
import com.bok.krypto.service.interfaces.MessageService;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;

import static com.bok.krypto.core.Constants.PENDING;

@Component
@Slf4j
public class TransferHelper {

    @Autowired
    KryptoHelper kryptoHelper;

    @Autowired
    WalletHelper walletHelper;

    @Autowired
    UserHelper userHelper;

    @Autowired
    TransferRepository transferRepository;

    @Autowired
    MessageService messageService;

    public TransferResponseDTO transfer(Long userId, TransferRequestDTO transferRequestDTO) {
        Preconditions.checkArgument(transferRequestDTO.amount.compareTo(BigDecimal.ZERO) > 0);
        Preconditions.checkArgument(userHelper.existsById(userId));
        Preconditions.checkArgument(walletHelper.existsByUserIdAndSymbol(userId, transferRequestDTO.symbol));
        if (!walletHelper.hasSufficientBalance(userId, transferRequestDTO.symbol, transferRequestDTO.amount)) {
            throw new InsufficientBalanceException("Insufficient balance");
        }
        Transfer t = new Transfer();
        t.setStatus(Activity.Status.PENDING);
        t = transferRepository.saveAndFlush(t);

        TransferMessage message = new TransferMessage();
        message.transferId = t.getId();
        message.userId = userId;
        message.symbol = transferRequestDTO.symbol;
        message.destination = transferRequestDTO.destination;
        message.amount = transferRequestDTO.amount;

        messageService.send(message);

        TransferResponseDTO response = new TransferResponseDTO();
        response.id = t.getId();
        response.status = PENDING;
        return response;
    }

    public TransferInfoDTO getTransferInfo(TransferInfoRequestDTO transferInfoRequestDTO) {
        Long transferId = transferInfoRequestDTO.transferId;
        Transfer t = transferRepository.findById(transferId).orElseThrow(() -> new TransactionNotFoundException("Could not find a transaction with the given ID"));
        TransferInfoDTO response = new TransferInfoDTO();
        response.id = t.getId();
        response.amount = t.getAmount();
        response.source = t.getSourceWallet().getId();
        response.destination = t.getDestinationWallet().getId();
        response.timestamp = t.getCreationTimestamp();
        response.status = t.getStatus().name();
        return response;
    }

    public TransfersInfoDTO getTransfersInfo(TransfersInfoRequestDTO transfersInfoRequestDTO) {
        List<Long> transfersIds = transfersInfoRequestDTO.transferIds;
        if (CollectionUtils.isEmpty(transfersIds)) {
            TransfersInfoDTO emptyDTO = new TransfersInfoDTO();

        }
        List<Transfer> transactions = transferRepository.findAllById(transfersIds);
        return null;
    }

    public void handle(TransferMessage transferMessage) {
        log.info("Processing transfer {}", transferMessage);
        Transfer t = transferRepository.findById(transferMessage.transferId).orElseThrow(() -> new RuntimeException("This transfer should have been persisted before"));
        Wallet source = walletHelper.findByUserIdAndSymbol(transferMessage.userId, transferMessage.symbol);
        Wallet destination = walletHelper.findById(transferMessage.destination);
        User user = userHelper.findById(transferMessage.userId);
        t.setSourceWallet(source);
        t.setDestinationWallet(destination);
        t.setAmount(transferMessage.amount);
        t.setUser(user);

        try {
            walletHelper.withdraw(source, transferMessage.amount);
        } catch (InsufficientBalanceException ex) {
            EmailMessage email = new EmailMessage();
            email.subject = "Insufficient Balance in your account";
            email.to = user.getEmail();
            email.text = "Your transfer of " + transferMessage.amount + " " + transferMessage.symbol + " has been DECLINED due to insufficient balance.";
            t.setStatus(Transaction.Status.REJECTED);
            messageService.send(email);
        }
        walletHelper.deposit(destination, transferMessage.amount);
        String u = userHelper.findEmailByUserId(transferMessage.userId);
        EmailMessage email = new EmailMessage();
        email.subject = "Transfer executed";
        email.to = u;
        email.text = "Your transfer of " + transferMessage.amount + " " + transferMessage.symbol + " has been ACCEPTED.";
        t.setStatus(Transaction.Status.SETTLED);
        transferRepository.saveAndFlush(t);
        messageService.send(email);
    }

    public StatusDTO getTransferStatus(Long transferId) {
        Transaction.Status status = transferRepository.findStatusById(transferId);
        StatusDTO statusDTO = new StatusDTO();
        statusDTO.status = status.name();
        return statusDTO;
    }

    public Integer pendingTransfers() {
        return transferRepository.countPendingTransfers();
    }
}
