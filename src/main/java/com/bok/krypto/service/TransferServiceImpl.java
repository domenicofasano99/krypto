package com.bok.krypto.service;

import com.bok.integration.StatusDTO;
import com.bok.integration.krypto.dto.TransferInfoDTO;
import com.bok.integration.krypto.dto.TransferInfoRequestDTO;
import com.bok.integration.krypto.dto.TransferRequestDTO;
import com.bok.integration.krypto.dto.TransferResponseDTO;
import com.bok.krypto.helper.TransferHelper;
import com.bok.krypto.service.interfaces.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransferServiceImpl implements TransferService {

    @Autowired
    TransferHelper transferHelper;

    @Override
    public TransferResponseDTO transfer(Long userId, TransferRequestDTO transferRequestDTO) {
        return transferHelper.transfer(userId, transferRequestDTO);
    }

    @Override
    public TransferInfoDTO transferInfo(Long userId, TransferInfoRequestDTO transferInfoRequestDTO) {
        return transferHelper.getTransferInfo(transferInfoRequestDTO);
    }

    @Override
    public StatusDTO transferStatus(Long userId, Long transferId) {
        return transferHelper.getTransferStatus(transferId);
    }
}
