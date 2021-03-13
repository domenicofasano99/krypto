package com.bok.krypto.service.interfaces;

import com.bok.integration.krypto.dto.WalletRequestDTO;
import com.bok.integration.krypto.dto.WalletResponseDTO;

public interface WalletService {

    WalletResponseDTO create(WalletRequestDTO walletRequestDTO);


}
