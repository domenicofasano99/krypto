package com.bok.krypto.service.interfaces;


import com.bok.integration.StatusDTO;
import com.bok.integration.krypto.dto.TransferInfoDTO;
import com.bok.integration.krypto.dto.TransferInfoRequestDTO;
import com.bok.integration.krypto.dto.TransferRequestDTO;
import com.bok.integration.krypto.dto.TransferResponseDTO;

public interface TransferService {

    TransferResponseDTO transfer(Long userId, TransferRequestDTO transferRequestDTO);

    TransferInfoDTO transferInfo(Long userId, TransferInfoRequestDTO transferInfoRequestDTO);

    StatusDTO transferStatus(Long userId, Long id);

    Integer pendingTransfers();
}
