package com.bok.krypto.service.interfaces;

import com.bok.krypto.dto.TransferRequestDTO;
import com.bok.krypto.dto.TransferResponseDTO;
import org.springframework.stereotype.Service;

@Service
public interface TransferService {

    TransferResponseDTO placeTransfer(TransferRequestDTO orderRequestDTO);
}
