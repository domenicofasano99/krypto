package com.bok.krypto.service.interfaces;


import com.bok.integration.krypto.dto.TransferInfoDTO;
import com.bok.integration.krypto.dto.TransferInfoRequestDTO;
import com.bok.integration.krypto.dto.TransferRequestDTO;
import com.bok.integration.krypto.dto.TransferResponseDTO;

public interface TransferService {

    TransferResponseDTO transfer(TransferRequestDTO transferRequestDTO);

    TransferInfoDTO transferInfo(TransferInfoRequestDTO transferInfoRequestDTO);
}
