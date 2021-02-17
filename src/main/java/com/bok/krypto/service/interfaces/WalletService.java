package com.bok.krypto.service.interfaces;

import com.bok.krypto.dto.WalletRequestDTO;
import com.bok.krypto.dto.WalletResponseDTO;

public interface WalletService {

    WalletResponseDTO createWallet(WalletRequestDTO walletRequestDTO);
}
