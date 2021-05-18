package com.bok.krypto.service.interfaces;


import com.bok.krypto.integration.internal.dto.TransferInfoDTO;
import com.bok.krypto.integration.internal.dto.TransferRequestDTO;
import com.bok.krypto.integration.internal.dto.TransferResponseDTO;

import java.util.UUID;

public interface TransferService {

    TransferResponseDTO transfer(Long accountId, TransferRequestDTO transferRequestDTO);

    TransferInfoDTO transferInfo(Long accountId, UUID transferId);

}
