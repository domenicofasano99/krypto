package com.bok.krypto.service.interfaces;

import com.bok.integration.krypto.WalletDeleteRequestDTO;
import com.bok.integration.krypto.WalletDeleteResponseDTO;
import com.bok.integration.krypto.WalletInfoDTO;
import com.bok.integration.krypto.WalletsDTO;
import com.bok.integration.krypto.dto.WalletRequestDTO;
import com.bok.integration.krypto.dto.WalletResponseDTO;

import java.util.UUID;

public interface WalletService {

    WalletResponseDTO create(Long userId, WalletRequestDTO walletRequestDTO);

    WalletDeleteResponseDTO delete(Long userId, WalletDeleteRequestDTO walletDeleteRequestDTO);

    WalletInfoDTO info(Long userId, UUID walletID);

    WalletsDTO wallets(Long userId);
}
