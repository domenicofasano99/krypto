package com.bok.krypto;

import com.bok.integration.krypto.dto.*;
import com.bok.krypto.model.Krypto;
import com.bok.krypto.repository.KryptoRepository;
import com.bok.krypto.service.interfaces.KryptoService;
import com.bok.krypto.utils.ModelTestUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@SpringBootTest
@Slf4j
class KryptoServiceTests {

    @Autowired
    KryptoService kryptoService;

    @Autowired
    ModelTestUtils modelTestUtils;

    @Autowired
    KryptoRepository kryptoRepository;

    @BeforeEach
    public void setup() {
        modelTestUtils.clearAll();
        modelTestUtils.populateDB();
    }

    @Test
    void getKryptoPrice() {
        List<Krypto> list = kryptoRepository.findAll();

        Krypto k = list.get(0);

        PriceResponseDTO responseDTO = kryptoService.getKryptoPrice(k.getSymbol());

        assertNotNull(responseDTO);
        assertNotNull(responseDTO.price);
        assertThat(responseDTO.price, is(k.getPrice()));

    }

    @Test
    void getKryptoInfo() {
        Krypto k = modelTestUtils.getRandomKrypto();
        KryptoInfoRequestDTO requestDTO = new KryptoInfoRequestDTO();
        requestDTO.symbol = k.getSymbol();
        KryptoInfoDTO responseDTO = kryptoService.getKryptoInfo(requestDTO);
        assertNotNull(responseDTO);
        assertEquals(responseDTO.price, k.getPrice());
        assertEquals(responseDTO.name, k.getName());
        assertEquals(responseDTO.networkFee, k.getNetworkFee());
        assertEquals(responseDTO.updateTimestamp, k.getUpdateTimestamp());
    }

    @Test
    void getKryptoInfos() {
        List<Krypto> list = kryptoRepository.findAll();
        KryptoInfosRequestDTO requestDTO = new KryptoInfosRequestDTO();
        requestDTO.symbols = list.stream().map(Krypto::getSymbol).collect(Collectors.toList());
        KryptoInfosDTO responseDTO = kryptoService.getKryptoInfos(requestDTO);
        for (KryptoInfoDTO kInfo : responseDTO.kryptos) {
            log.info("Analyzing response for Krypto: {}", kInfo.symbol);
            assertEquals(kInfo.name, list.stream().filter(k -> k.getName().equals(kInfo.name)).findFirst().get().getName());
            assertEquals(kInfo.symbol, list.stream().filter(k -> k.getSymbol().equals(kInfo.symbol)).findFirst().get().getSymbol());
            assertEquals(kInfo.price, list.stream().filter(k -> k.getPrice().equals(kInfo.price)).findFirst().get().getPrice());
            assertEquals(kInfo.updateTimestamp, list.stream().filter(k -> k.getUpdateTimestamp().equals(kInfo.updateTimestamp)).findFirst().get().getUpdateTimestamp());
        }

    }

    @Test
    void getKryptoHistoricalData() {
        Krypto k = modelTestUtils.getRandomKrypto();
        modelTestUtils.generateRandomHistoricalData(k, Instant.parse("2007-12-03T10:15:30.00Z"), Instant.now(), 300L);

        HistoricalDataRequestDTO requestDTO = new HistoricalDataRequestDTO();
        requestDTO.start = Instant.parse("2020-12-03T10:15:30.00Z");
        requestDTO.end = Instant.now();
        requestDTO.symbol = k.getSymbol();
        HistoricalDataDTO response = kryptoService.getKryptoHistoricalData(requestDTO);
        assertNotNull(response.history);
        log.info(String.valueOf(response.history));
    }
}
