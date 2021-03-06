package com.bok.krypto.service;

import com.bok.krypto.helper.WalletHelper;
import com.bok.krypto.integration.internal.dto.ValidationRequestDTO;
import com.bok.krypto.integration.internal.dto.WalletDeleteRequestDTO;
import com.bok.krypto.integration.internal.dto.WalletDeleteResponseDTO;
import com.bok.krypto.integration.internal.dto.WalletInfoDTO;
import com.bok.krypto.integration.internal.dto.WalletRequestDTO;
import com.bok.krypto.integration.internal.dto.WalletResponseDTO;
import com.bok.krypto.integration.internal.dto.WalletsDTO;
import com.bok.krypto.service.interfaces.WalletService;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

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
    public WalletInfoDTO info(Long accountId, String symbol, Instant startDate, Instant endDate) {
        Preconditions.checkNotNull(accountId);
        Preconditions.checkNotNull(symbol, "wallet symbol cannot be null");
        return walletHelper.info(accountId, symbol, startDate, endDate);
    }

    @Override
    public WalletsDTO listWallets(Long accountId) {
        Preconditions.checkNotNull(accountId);
        return walletHelper.getWallets(accountId);
    }

    @Override
    public Boolean validateAddress(ValidationRequestDTO requestDTO) {
        Preconditions.checkNotNull(requestDTO);
        Preconditions.checkNotNull(requestDTO.symbol, "wallet symbol cannot be null");
        Preconditions.checkNotNull(requestDTO.address, "wallet address cannot be null");
        return walletHelper.validateAddress(requestDTO);
    }
}
