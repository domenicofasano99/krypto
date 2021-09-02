package com.bok.krypto.helper;

import com.bok.bank.integration.util.Money;
import com.bok.krypto.core.Constants;
import com.bok.krypto.exception.TransactionException;
import com.bok.krypto.exception.TransactionNotFoundException;
import com.bok.krypto.integration.internal.dto.StatusDTO;
import com.bok.krypto.integration.internal.dto.TransferInfoDTO;
import com.bok.krypto.integration.internal.dto.TransferRequestDTO;
import com.bok.krypto.integration.internal.dto.TransferResponseDTO;
import com.bok.krypto.integration.internal.dto.TransfersInfoDTO;
import com.bok.krypto.integration.internal.dto.TransfersInfoRequestDTO;
import com.bok.krypto.messaging.messages.TransferMessage;
import com.bok.krypto.model.Account;
import com.bok.krypto.model.Activity;
import com.bok.krypto.model.Transaction;
import com.bok.krypto.model.Transfer;
import com.bok.krypto.model.Wallet;
import com.bok.krypto.repository.TransferRepository;
import com.bok.krypto.service.bank.BankService;
import com.bok.krypto.service.interfaces.MessageService;
import com.bok.parent.integration.message.EmailMessage;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Currency;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

    @Autowired
    BankService bankService;

    @Autowired
    MarketHelper marketHelper;

    public TransferResponseDTO transfer(Long accountId, TransferRequestDTO requestDTO) {

        Account account = accountHelper.findById(accountId);
        Wallet source = walletHelper.findByPublicId(requestDTO.source);

        Preconditions.checkNotNull(account);
        Preconditions.checkArgument(!requestDTO.source.equalsIgnoreCase(requestDTO.destination), "Source and destination coincide.");
        Preconditions.checkArgument(requestDTO.amount.compareTo(BigDecimal.ZERO) > 0);
        Preconditions.checkArgument(walletHelper.existsByAccountIdAndSymbol(accountId, requestDTO.symbol));

        Money money = new Money(Currency.getInstance(requestDTO.currencyCode), requestDTO.amount);

        BigDecimal amount = marketHelper.getKryptoAmount(source.getKrypto(), money);
        if (source.getAvailableAmount().compareTo(amount) < 0) {
            throw new TransactionException("Insufficient balance");
        }

        Transfer transfer = new Transfer();
        transfer = transferRepository.saveAndFlush(transfer);

        TransferMessage message = new TransferMessage();
        message.transferId = transfer.getId();
        message.accountId = accountId;
        message.symbol = requestDTO.symbol;
        message.destination = requestDTO.destination;
        message.amount = amount;

        messageService.sendTransfer(message);

        TransferResponseDTO response = new TransferResponseDTO();
        response.publicId = transfer.getPublicId();
        response.status = Constants.PENDING;
        return response;
    }

    @Transactional
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
        } catch (TransactionException ex) {
            EmailMessage email = new EmailMessage();
            email.subject = "Insufficient Balance in your account";
            email.to = accountHelper.getEmailByAccountId(account.getId());
            email.body = "Your transfer of " + transferMessage.amount + " " + transferMessage.symbol + " has been DECLINED due to insufficient balance.";
            transfer.setStatus(Activity.Status.DECLINED);
            messageService.sendEmail(email);
        }
        EmailMessage email = new EmailMessage();
        email.subject = "Transfer executed";
        email.to = accountHelper.getEmailByAccountId(account.getId());
        email.body = "Your transfer of " + transferMessage.amount + " " + transferMessage.symbol + " has been ACCEPTED.";
        transfer.setStatus(Activity.Status.SETTLED);
        transferRepository.saveAndFlush(transfer);
        messageService.sendEmail(email);
    }

    @Transactional
    public void transfer(Wallet source, Wallet destination, BigDecimal amount) {
        BigDecimal withdrawn = walletHelper.withdraw(source, amount);
        walletHelper.deposit(destination, withdrawn);
    }

    public TransferInfoDTO getTransferInfo(Long userId, UUID transferId) {
        accountHelper.existsById(userId);
        Transfer t = transferRepository.findByPublicId(transferId).orElseThrow(() -> new TransactionNotFoundException("Could not find a transaction with the given ID"));
        TransferInfoDTO response = new TransferInfoDTO();
        response.publicId = t.getPublicId();
        response.amount = t.getAmount();
        response.source = t.getSourceWallet().getAddress();
        response.destination = t.getDestinationWallet().getAddress();
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


    public StatusDTO getTransferStatus(Long transferId) {
        Transaction.Status status = transferRepository.findStatusById(transferId);
        StatusDTO statusDTO = new StatusDTO();
        statusDTO.status = status.name();
        return statusDTO;
    }

    public Integer pendingTransfers() {
        return transferRepository.countPendingTransfers();
    }

    public List<Transfer> findByWalletIdAndDateBetween(Wallet wallet, Instant startDate, Instant endDate) {
        List<Transfer> transfers = transferRepository.findBySourceWallet_IdOrDestinationWallet_Id(wallet.getId(), wallet.getId());
        return transfers.stream()
                .filter(t -> t.getCreationTimestamp().isAfter(startDate)
                        && t.getCreationTimestamp().isBefore(endDate))
                .collect(Collectors.toList());
        //return transferRepository.findByAndCreationTimestampBetween(wallet.getId(), startDate.atStartOfDay().toInstant(ZoneOffset.UTC), endDate.atStartOfDay().toInstant(ZoneOffset.UTC));
    }
}
