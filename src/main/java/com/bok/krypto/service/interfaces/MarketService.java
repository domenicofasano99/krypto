package com.bok.krypto.service.interfaces;

import com.bok.integration.krypto.PurchaseRequestDTO;
import com.bok.integration.krypto.dto.SellRequestDTO;
import com.bok.integration.krypto.dto.TransactionDTO;

public interface MarketService {

    TransactionDTO buy(Long userId, PurchaseRequestDTO purchaseRequestDTO);

    TransactionDTO sell(Long userId, SellRequestDTO sellRequestDTO);
}
