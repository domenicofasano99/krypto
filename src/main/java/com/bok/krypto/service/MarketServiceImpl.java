package com.bok.krypto.service;

import com.bok.integration.krypto.PurchaseRequestDTO;
import com.bok.integration.krypto.dto.SellRequestDTO;
import com.bok.integration.krypto.dto.TransactionDTO;
import com.bok.krypto.helper.KryptoHelper;
import com.bok.krypto.helper.MarketHelper;
import com.bok.krypto.helper.UserHelper;
import com.bok.krypto.helper.WalletHelper;
import com.bok.krypto.service.interfaces.MarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MarketServiceImpl implements MarketService {

    @Autowired
    MarketHelper marketHelper;

    @Autowired
    WalletHelper walletHelper;

    @Autowired
    UserHelper userHelper;

    @Autowired
    KryptoHelper kryptoHelper;

    @Override
    public TransactionDTO buy(Long userId, PurchaseRequestDTO purchaseRequestDTO) {
        return marketHelper.buy(userId, purchaseRequestDTO);
    }

    @Override
    public TransactionDTO sell(Long userId, SellRequestDTO sellRequestDTO) {
        return marketHelper.sell(userId, sellRequestDTO);
    }
}
