package com.bok.krypto.service;

import com.bok.krypto.integration.internal.dto.WalletDeleteRequestDTO;
import com.bok.krypto.integration.internal.dto.WalletDeleteResponseDTO;
import com.bok.krypto.integration.internal.dto.WalletInfoDTO;
import com.bok.krypto.integration.internal.dto.WalletsDTO;
import com.bok.krypto.integration.internal.dto.WalletRequestDTO;
import com.bok.krypto.integration.internal.dto.WalletResponseDTO;
import com.bok.krypto.helper.WalletHelper;
import com.bok.krypto.service.interfaces.WalletService;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WalletServiceImpl implements WalletService {

    @Autowired
    WalletHelper walletHelper;

    @Override
    public WalletResponseDTO create(Long accountId, WalletRequestDTO walletRequestDTO) {
        Preconditions.checkNotNull(accountId);
        Preconditions.checkNotNull(walletRequestDTO.symbol, "Symbol cannot be null");
        return walletHelper.createWallet(accountId, walletRequestDTO);
    }

    @Override
    public WalletDeleteResponseDTO delete(Long accountId, WalletDeleteRequestDTO walletDeleteRequestDTO) {
        Preconditions.checkNotNull(accountId);
        Preconditions.checkNotNull(walletDeleteRequestDTO.symbol, "Symbol cannot be null");
        return walletHelper.delete(accountId, walletDeleteRequestDTO);
    }

    @Override
    public WalletInfoDTO info(Long accountId, String walletId) {
        Preconditions.checkNotNull(accountId);
        Preconditions.checkNotNull(walletId, "wallet ID cannot be null");
        return walletHelper.info(accountId, walletId);
    }

    @Override
    public WalletsDTO wallets(Long accountId) {
        Preconditions.checkNotNull(accountId);
        return walletHelper.wallets(accountId);
    }
}
