package com.bok.krypto.service.interfaces;


import com.bok.krypto.integration.internal.dto.TransferInfoDTO;
import com.bok.krypto.integration.internal.dto.TransferRequestDTO;
import com.bok.krypto.integration.internal.dto.TransferResponseDTO;

public interface TransferService {

    TransferResponseDTO transfer(Long accountId, TransferRequestDTO transferRequestDTO);

    TransferInfoDTO transferInfo(Long accountId, String transferId);

}
