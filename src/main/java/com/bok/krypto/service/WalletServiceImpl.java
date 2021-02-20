package com.bok.krypto.service;

import com.bok.krypto.dto.WalletRequestDTO;
import com.bok.krypto.dto.WalletResponseDTO;
import com.bok.krypto.service.interfaces.WalletService;
import org.springframework.stereotype.Service;

@Service
public class WalletServiceImpl implements WalletService {
    @Override
    public WalletResponseDTO create(WalletRequestDTO walletRequestDTO) {
        return null;
    }
}
