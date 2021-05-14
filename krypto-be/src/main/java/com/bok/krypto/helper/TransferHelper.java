package com.bok.krypto.helper;

import com.bok.parent.integration.message.EmailMessage;
import com.bok.krypto.integration.internal.dto.StatusDTO;
import com.bok.krypto.integration.internal.dto.TransfersInfoDTO;
import com.bok.krypto.integration.internal.dto.TransfersInfoRequestDTO;
import com.bok.krypto.integration.internal.dto.TransferInfoDTO;
import com.bok.krypto.integration.internal.dto.TransferRequestDTO;
import com.bok.krypto.integration.internal.dto.TransferResponseDTO;
import com.bok.krypto.core.Constants;
import com.bok.krypto.exception.InsufficientBalanceException;
import com.bok.krypto.exception.TransactionNotFoundException;
import com.bok.krypto.messaging.internal.messages.TransferMessage;
import com.bok.krypto.model.Account;
import com.bok.krypto.model.Activity;
import com.bok.krypto.model.Transaction;
import com.bok.krypto.model.Transfer;
import com.bok.krypto.model.Wallet;
import com.bok.krypto.repository.TransferRepository;
import com.bok.krypto.service.interfaces.MessageService;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;

@Component
@Slf4j
public class TransferHelper {

    @Autowired
    KryptoHelper kryptoHelper;

    @Autowired
    WalletHelper walletHelper;

    @Autowired
    AccountHelper accountHelper;

    @Autowired
    TransferRepository transferRepository;

    @Autowired
    MessageService messageService;

    public TransferResponseDTO transfer(Long accountId, TransferRequestDTO transferRequestDTO) {

        Account a = accountHelper.findById(accountId);
        Wallet source = walletHelper.findByPublicId(transferRequestDTO.source);

        Preconditions.checkNotNull(a);
        //Preconditions.checkArgument(accountHelper.checkRightsOnResource(a, source));
        Preconditions.checkArgument(transferRequestDTO.amount.compareTo(BigDecimal.ZERO) > 0);
        Preconditions.checkArgument(walletHelper.existsByUserIdAndSymbol(accountId, transferRequestDTO.symbol));
        if (!walletHelper.hasSufficientBalance(accountId, transferRequestDTO.symbol, transferRequestDTO.amount)) {
            throw new InsufficientBalanceException("Insufficient balance");
        }
        Transfer t = new Transfer();
        t = transferRepository.saveAndFlush(t);

        TransferMessage message = new TransferMessage();
        message.transferId = t.getId();
        message.accountId = accountId;
        message.symbol = transferRequestDTO.symbol;
        message.destination = transferRequestDTO.destination;
        message.amount = transferRequestDTO.amount;

        messageService.sendTransfer(message);

        TransferResponseDTO response = new TransferResponseDTO();
        response.publicId = t.getPublicId();
        response.status = Constants.PENDING;
        return response;
    }

    public TransferInfoDTO getTransferInfo(Long userId, String transferId) {
        accountHelper.existsById(userId);
        Transfer t = transferRepository.findByPublicId(transferId).orElseThrow(() -> new TransactionNotFoundException("Could not find a transaction with the given ID"));
        TransferInfoDTO response = new TransferInfoDTO();
        response.publicId = t.getPublicId();
        response.amount = t.getAmount();
        response.source = t.getSourceWallet().getPublicId();
        response.destination = t.getDestinationWallet().getPublicId();
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
        Account account = accountHelper.findById(transferMessage.accountId);
        Transfer transfer = transferRepository.findById(transferMessage.transferId).orElseThrow(() -> new RuntimeException("This transfer should have been persisted before"));
        Wallet source = walletHelper.findByAccountIdAndSymbol(transferMessage.accountId, transferMessage.symbol);
        Wallet destination = walletHelper.findByPublicId(transferMessage.destination);

        transfer.setSourceWallet(source);
        transfer.setDestinationWallet(destination);
        transfer.setAmount(transferMessage.amount);
        transfer.setAccount(account);

        try {
            transfer(source, destination, transferMessage.amount);
        } catch (InsufficientBalanceException ex) {
            EmailMessage email = new EmailMessage();
            email.subject = "Insufficient Balance in your account";
            email.to = account.getEmail();
            email.text = "Your transfer of " + transferMessage.amount + " " + transferMessage.symbol + " has been DECLINED due to insufficient balance.";
            transfer.setStatus(Activity.Status.REJECTED);
            messageService.sendEmail(email);
        }
        EmailMessage email = new EmailMessage();
        email.subject = "Transfer executed";
        email.to = account.getEmail();
        email.text = "Your transfer of " + transferMessage.amount + " " + transferMessage.symbol + " has been ACCEPTED.";
        transfer.setStatus(Activity.Status.SETTLED);
        transferRepository.saveAndFlush(transfer);
        messageService.sendEmail(email);
    }

    public void transfer(Wallet source, Wallet destination, BigDecimal amount) {
        BigDecimal withdrawn = walletHelper.withdraw(source, amount);
        walletHelper.deposit(destination, withdrawn);
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
