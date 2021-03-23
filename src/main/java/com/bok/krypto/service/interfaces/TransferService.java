package com.bok.krypto.service.interfaces;


import com.bok.integration.krypto.dto.TransferInfoDTO;
import com.bok.integration.krypto.dto.TransferRequestDTO;
import com.bok.integration.krypto.dto.TransferResponseDTO;

public interface TransferService {

    TransferResponseDTO transfer(Long userId, TransferRequestDTO transferRequestDTO);

    TransferInfoDTO transferInfo(Long userId, Long transferId);

}
