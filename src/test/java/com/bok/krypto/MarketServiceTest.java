package com.bok.krypto;

import com.bok.integration.krypto.PurchaseRequestDTO;
import com.bok.integration.krypto.dto.TransactionDTO;
import com.bok.krypto.model.Krypto;
import com.bok.krypto.model.Transaction;
import com.bok.krypto.model.User;
import com.bok.krypto.model.Wallet;
import com.bok.krypto.repository.TransactionRepository;
import com.bok.krypto.service.interfaces.MarketService;
import com.bok.krypto.utils.ModelTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static com.bok.krypto.utils.Constants.BTC;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class MarketServiceTest {

    @Autowired
    ModelTestUtils modelTestUtils;

    @Autowired
    MarketService marketService;

    @Autowired
    TransactionRepository transactionRepository;

    @BeforeEach
    public void init() {
        modelTestUtils.clearAll();
        modelTestUtils.populateDB();
    }

    @Test
    public void purchaseTest() {
        User u = modelTestUtils.createUser();
        Krypto k = modelTestUtils.getKrypto(BTC);
        Wallet w = modelTestUtils.createWallet(u, k, new BigDecimal("0"));

        PurchaseRequestDTO purchaseRequestDTO = new PurchaseRequestDTO();
        purchaseRequestDTO.symbol = BTC;
        purchaseRequestDTO.amount = new BigDecimal("0.8989827");
        TransactionDTO transactionDTO = marketService.buy(u.getId(), purchaseRequestDTO);
        modelTestUtils.await();
        Transaction t = transactionRepository.findById(transactionDTO.id).get();
        assertNotNull(t);
        assertEquals(t.getId(), transactionDTO.id);
        assertEquals(Transaction.Status.SETTLED, t.getStatus());

    }


}
