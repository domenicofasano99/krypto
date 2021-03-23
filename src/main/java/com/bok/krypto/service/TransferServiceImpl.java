package com.bok.krypto.service;

import com.bok.integration.krypto.dto.TransferInfoDTO;
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
    public TransferInfoDTO transferInfo(Long userId, Long transferId) {
        return transferHelper.getTransferInfo(userId, transferId);
    }
}
