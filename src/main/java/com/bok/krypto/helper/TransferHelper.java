package com.bok.krypto.helper;

import com.bok.integration.TransfersInfoDTO;
import com.bok.integration.TransfersInfoRequestDTO;
import com.bok.integration.krypto.dto.TransferInfoDTO;
import com.bok.integration.krypto.dto.TransferInfoRequestDTO;
import com.bok.integration.krypto.dto.TransferRequestDTO;
import com.bok.integration.krypto.dto.TransferResponseDTO;
import com.bok.krypto.exception.TransactionNotFoundException;
import com.bok.krypto.model.Transaction;
import com.bok.krypto.model.User;
import com.bok.krypto.model.Wallet;
import com.bok.krypto.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

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

    public TransferResponseDTO transfer(Long userId, TransferRequestDTO transferRequestDTO) {
        if (transferRequestDTO.amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Cannot transfer negative amount");
        }
        User user = userHelper.findById(userId);
        Wallet source = walletHelper.findById(transferRequestDTO.from);
        Wallet destination = walletHelper.findById(transferRequestDTO.to);
        Transaction transaction = walletHelper.transfer(user, source, destination, transferRequestDTO.amount);
        if (Objects.nonNull(transaction)) {
            TransferResponseDTO response = new TransferResponseDTO();
            response.status = transaction.getStatus().name();
            response.id = transaction.getId();
            response.amount = transaction.getAmount();
            response.source = transaction.getSourceWallet().getId();
            response.destination = transaction.getDestinationWallet().getId();
            return response;
        }
        throw new RuntimeException("Error while performing the transfer");
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
}
