package com.bok.krypto;

import com.bok.krypto.core.MarketData;
import com.bok.krypto.external.CoinMarketAPI;
import com.bok.krypto.model.Krypto;
import com.bok.krypto.repository.KryptoRepository;
import com.bok.krypto.utils.ModelTestUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Slf4j
public class DataGatheringTest {

    @Autowired
    ModelTestUtils modelTestUtils;

    @Autowired
    CoinMarketAPI coinMarketAPI;

    @Autowired
    KryptoRepository kryptoRepository;

    @Autowired
    MarketData marketData;

    @BeforeEach
    public void beforeEach() {
        modelTestUtils.clearAll();
        modelTestUtils.populateDB();
    }


    @Ignore //don't run this test if not strictly necessary, it consumes API credit
    public void testImportFromCoinMarketAPI() {
        modelTestUtils.clearAll();

        List<Krypto> list = kryptoRepository.findAll();
        assert list.size() == 0;
        marketData.fetchData();
        list = kryptoRepository.findAll();
        assert list.size() == 10;

        Krypto k = list.get(0);

        assert k.getName() != null;
        log.info(k.getName());

        assert k.getPrice() != null;
        log.info(k.getPrice().toString());

        assert k.getSymbol() != null;
        log.info(k.getSymbol());

    }
}
