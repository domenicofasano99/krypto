package com.bok.krypto.service;

import com.bok.integration.krypto.WalletDeleteRequestDTO;
import com.bok.integration.krypto.WalletDeleteResponseDTO;
import com.bok.integration.krypto.WalletInfoDTO;
import com.bok.integration.krypto.WalletsDTO;
import com.bok.integration.krypto.dto.WalletRequestDTO;
import com.bok.integration.krypto.dto.WalletResponseDTO;
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
