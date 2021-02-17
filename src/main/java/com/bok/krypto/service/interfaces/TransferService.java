package com.bok.krypto.service.interfaces;

import com.bok.krypto.dto.TransferRequestDTO;
import com.bok.krypto.dto.TransferResponseDTO;

public interface TransferService {

    TransferResponseDTO placeTransfer(TransferRequestDTO orderRequestDTO);
}
