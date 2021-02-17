package com.bok.krypto.service.interfaces;

import com.bok.krypto.dto.WalletRequestDTO;
import com.bok.krypto.dto.WalletResponseDTO;
import org.springframework.stereotype.Service;

@Service
public interface WalletService {

    WalletResponseDTO createWallet(WalletRequestDTO walletRequestDTO);
}
