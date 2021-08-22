package com.bok.krypto.service.interfaces;

import com.bok.krypto.integration.internal.dto.*;

import java.time.Instant;

public interface WalletService {

    WalletResponseDTO create(Long userId, WalletRequestDTO walletRequestDTO);

    WalletDeleteResponseDTO delete(Long userId, WalletDeleteRequestDTO walletDeleteRequestDTO);

    WalletInfoDTO info(Long userId, String symbol, Instant startDate, Instant endDate);

    WalletsDTO listWallets(Long userId);
}
