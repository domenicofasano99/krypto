package com.bok.krypto.service;

import com.bok.integration.krypto.WalletDeleteRequestDTO;
import com.bok.integration.krypto.WalletDeleteResponseDTO;
import com.bok.integration.krypto.WalletInfoDTO;
import com.bok.integration.krypto.WalletsDTO;
import com.bok.integration.krypto.dto.WalletRequestDTO;
import com.bok.integration.krypto.dto.WalletResponseDTO;
import com.bok.krypto.helper.WalletHelper;
import com.bok.krypto.service.interfaces.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class WalletServiceImpl implements WalletService {

    @Autowired
    WalletHelper walletHelper;

    @Override
    public WalletResponseDTO create(Long userId, WalletRequestDTO walletRequestDTO) {
        return walletHelper.createWallet(userId, walletRequestDTO);
    }

    @Override
    public WalletDeleteResponseDTO delete(Long userId, WalletDeleteRequestDTO walletDeleteRequestDTO) {
        return walletHelper.delete(userId, walletDeleteRequestDTO);
    }

    @Override
    public WalletInfoDTO info(Long userId, UUID walletID) {
        return walletHelper.info(userId, walletID);
    }

    @Override
    public WalletsDTO wallets(Long userId) {
        return walletHelper.wallets(userId);
    }
}
