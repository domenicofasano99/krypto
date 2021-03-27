package com.bok.krypto.market;

import com.bok.integration.UserBalance;
import com.bok.integration.krypto.PurchaseRequestDTO;
import com.bok.integration.krypto.dto.*;
import com.bok.krypto.model.*;
import com.bok.krypto.repository.HistoricalDataRepository;
import com.bok.krypto.repository.KryptoRepository;
import com.bok.krypto.repository.TransactionRepository;
import com.bok.krypto.service.bank.BankService;
import com.bok.krypto.service.interfaces.MarketService;
import com.bok.krypto.utils.ModelTestUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import static com.bok.krypto.utils.Constants.BTC;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
@Slf4j
public class MarketServiceTest {

    @Autowired
    ModelTestUtils modelTestUtils;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    KryptoRepository kryptoRepository;

    @Autowired
    HistoricalDataRepository historicalDataRepository;

    @MockBean
    BankService bankService;

    @Autowired
    MarketService marketService;// = new MarketServiceImpl();

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
        when(bankService.getUserBalance(any())).thenReturn(new UserBalance(u.getId(), new BigDecimal("10000")));
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

    @Test
    public void getKryptoPrice() {

        User u = modelTestUtils.createUser();
        List<Krypto> list = kryptoRepository.findAll();

        Krypto k = list.get(0);

        PriceResponseDTO responseDTO = marketService.getKryptoPrice(k.getSymbol());

        Assert.assertNotNull(responseDTO);
        Assert.assertNotNull(responseDTO.price);
        assertThat(responseDTO.price, is(k.getPrice()));

    }

    @Test
    public void getKryptoInfo() {
        User u = modelTestUtils.createUser();
        Krypto k = modelTestUtils.getRandomKrypto();
        KryptoInfoDTO responseDTO = marketService.getKryptoInfo(k.getSymbol());
        Assert.assertNotNull(responseDTO);
        Assert.assertEquals(responseDTO.price, k.getPrice());
        Assert.assertEquals(responseDTO.name, k.getName());
        Assert.assertEquals(responseDTO.networkFee, k.getNetworkFee());
        Assert.assertEquals(responseDTO.updateTimestamp, k.getUpdateTimestamp());
    }

    @Test
    public void getKryptoInfos() {
        User u = modelTestUtils.createUser();
        List<Krypto> list = kryptoRepository.findAll();
        KryptoInfosRequestDTO requestDTO = new KryptoInfosRequestDTO();
        requestDTO.symbols = list.stream().map(Krypto::getSymbol).collect(Collectors.toList());
        KryptoInfosDTO responseDTO = marketService.getKryptoInfos(requestDTO);
        for (KryptoInfoDTO kInfo : responseDTO.kryptos) {
            log.info("Analyzing response for Krypto: {}", kInfo.symbol);
            Assert.assertEquals(kInfo.name, list.stream().filter(k -> k.getName().equals(kInfo.name)).findFirst().get().getName());
            Assert.assertEquals(kInfo.symbol, list.stream().filter(k -> k.getSymbol().equals(kInfo.symbol)).findFirst().get().getSymbol());
            Assert.assertEquals(kInfo.price, list.stream().filter(k -> k.getPrice().equals(kInfo.price)).findFirst().get().getPrice());
            Assert.assertEquals(kInfo.updateTimestamp, list.stream().filter(k -> k.getUpdateTimestamp().equals(kInfo.updateTimestamp)).findFirst().get().getUpdateTimestamp());
        }

    }

    @Test
    public void getKryptoHistoricalData() {
        User u = modelTestUtils.createUser();
        Krypto k = modelTestUtils.getRandomKrypto();

        HistoricalDataRequestDTO requestDTO = new HistoricalDataRequestDTO();
        requestDTO.start = Instant.parse("2020-12-03T10:15:30.00Z");
        requestDTO.end = Instant.now();
        requestDTO.symbol = k.getSymbol();
        HistoricalDataDTO response = marketService.getKryptoHistoricalData(requestDTO);
        Assert.assertNotNull(response.history);
        log.info(String.valueOf(response.history));
        for (RecordDTO datum : response.history) {
            HistoricalData savedDatum = historicalDataRepository.findById(datum.id).get();
            Assert.assertEquals(savedDatum.getId(), datum.id);
            Assert.assertEquals(savedDatum.getPrice(), datum.price);
        }
    }


    @Ignore
    public void evaluatePerformance() {
        int kryptos = 1000;
        int records = 1000;
        long start = Instant.now().toEpochMilli();
        modelTestUtils.generateDatabaseRandomNoise(kryptos, records);
        long end = Instant.now().toEpochMilli();
        long elapsed = end - start;
        long opsPorMillis = (long) kryptos * records / elapsed;
        log.info("Geneating {} Historical records for {} Kryptos took {} ms, performing {}ops/ms", kryptos, kryptos, elapsed, opsPorMillis);
    }

    @Test
    public void deniedPurchaseTest_insufficientBalance() {
        User u = modelTestUtils.createUser();
        Krypto k = modelTestUtils.getKrypto(BTC);
        when(bankService.getUserBalance(any())).thenReturn(new UserBalance(u.getId(), new BigDecimal("0")));

        PurchaseRequestDTO purchaseRequest = new PurchaseRequestDTO();
        purchaseRequest.amount = new BigDecimal("0.012001023");
        purchaseRequest.symbol = BTC;

        assertThrows(RuntimeException.class, () -> marketService.buy(u.getId(), purchaseRequest));
    }

    @Test
    public void purchaseTest_permitted() {
        User u = modelTestUtils.createUser();
        Krypto k = modelTestUtils.getKrypto(BTC);
        when(bankService.getUserBalance(anyLong())).thenReturn(new UserBalance(u.getId(), new BigDecimal("10000")));

        PurchaseRequestDTO purchaseRequest = new PurchaseRequestDTO();
        purchaseRequest.amount = new BigDecimal("0.012001023");
        purchaseRequest.symbol = BTC;

        TransactionDTO response = marketService.buy(u.getId(), purchaseRequest);
        assertNotNull(response.id);
    }

}
