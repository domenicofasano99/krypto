package com.bok.krypto.service;

import com.bok.krypto.dto.TransferRequestDTO;
import com.bok.krypto.dto.TransferResponseDTO;
import com.bok.krypto.service.interfaces.TransferService;
import org.springframework.stereotype.Service;

@Service
public class TransferServiceImpl implements TransferService {
    @Override
    public TransferResponseDTO placeTransfer(TransferRequestDTO orderRequestDTO) {
        return null;
    }
}
