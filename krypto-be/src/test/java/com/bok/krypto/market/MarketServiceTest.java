package com.bok.krypto.market;

import com.bok.integration.UserBalance;
import com.bok.integration.krypto.PurchaseRequestDTO;
import com.bok.integration.krypto.dto.HistoricalDataDTO;
import com.bok.integration.krypto.dto.HistoricalDataRequestDTO;
import com.bok.integration.krypto.dto.KryptoInfoDTO;
import com.bok.integration.krypto.dto.KryptoInfosDTO;
import com.bok.integration.krypto.dto.KryptoInfosRequestDTO;
import com.bok.integration.krypto.dto.PriceResponseDTO;
import com.bok.integration.krypto.dto.RecordDTO;
import com.bok.integration.krypto.dto.TransactionDTO;
import com.bok.krypto.model.Account;
import com.bok.krypto.model.HistoricalData;
import com.bok.krypto.model.Krypto;
import com.bok.krypto.model.Transaction;
import com.bok.krypto.model.Wallet;
import com.bok.krypto.repository.HistoricalDataRepository;
import com.bok.krypto.repository.KryptoRepository;
import com.bok.krypto.repository.TransactionRepository;
import com.bok.krypto.service.bank.BankService;
import com.bok.krypto.service.interfaces.MarketService;
import com.bok.krypto.utils.ModelTestUtils;
import lombok.extern.slf4j.Slf4j;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    MarketService marketService;

    @BeforeEach
    public void init() {
        modelTestUtils.clearAll();
        modelTestUtils.populateDB();
    }

    @Test
    public void purchaseTest() {
        Account u = modelTestUtils.createAccount();
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

        Account u = modelTestUtils.createAccount();
        List<Krypto> list = kryptoRepository.findAll();

        Krypto k = list.get(0);

        PriceResponseDTO responseDTO = marketService.getKryptoPrice(k.getSymbol());

        assertNotNull(responseDTO);
        assertNotNull(responseDTO.price);
        assertThat(responseDTO.price, is(k.getPrice()));

    }

    @Test
    public void getKryptoInfo() {
        Account u = modelTestUtils.createAccount();
        Krypto k = modelTestUtils.getRandomKrypto();
        KryptoInfoDTO responseDTO = marketService.getKryptoInfo(k.getSymbol());
        assertNotNull(responseDTO);
        assertEquals(responseDTO.price, k.getPrice());
        assertEquals(responseDTO.name, k.getName());
        assertEquals(responseDTO.networkFee, k.getNetworkFee());
        assertEquals(responseDTO.updateTimestamp, k.getUpdateTimestamp());
    }

    @Test
    public void getKryptoInfos() {
        Account u = modelTestUtils.createAccount();
        List<Krypto> list = kryptoRepository.findAll();
        KryptoInfosRequestDTO requestDTO = new KryptoInfosRequestDTO();
        requestDTO.symbols = list.stream().map(Krypto::getSymbol).collect(Collectors.toList());
        KryptoInfosDTO responseDTO = marketService.getKryptoInfos(requestDTO);
        for (KryptoInfoDTO kInfo : responseDTO.kryptos) {
            log.info("Analyzing response for Krypto: {}", kInfo.symbol);
            assertEquals(kInfo.name, list.stream().filter(k -> k.getName().equals(kInfo.name)).findFirst().get().getName());
            assertEquals(kInfo.symbol, list.stream().filter(k -> k.getSymbol().equals(kInfo.symbol)).findFirst().get().getSymbol());
            assertEquals(kInfo.price, list.stream().filter(k -> k.getPrice().equals(kInfo.price)).findFirst().get().getPrice());
            assertEquals(kInfo.updateTimestamp, list.stream().filter(k -> k.getUpdateTimestamp().equals(kInfo.updateTimestamp)).findFirst().get().getUpdateTimestamp());
        }

    }

    @Test
    public void getKryptoHistoricalData() {
        Account u = modelTestUtils.createAccount();
        Krypto k = modelTestUtils.getRandomKrypto();

        HistoricalDataRequestDTO requestDTO = new HistoricalDataRequestDTO();
        requestDTO.start = Instant.parse("2020-12-03T10:15:30.00Z");
        requestDTO.end = Instant.now();
        requestDTO.symbol = k.getSymbol();
        HistoricalDataDTO response = marketService.getKryptoHistoricalData(requestDTO);
        assertNotNull(response.history);
        log.info(String.valueOf(response.history));
        for (RecordDTO datum : response.history) {
            HistoricalData savedDatum = historicalDataRepository.findById(datum.id).get();
            assertEquals(savedDatum.getId(), datum.id);
            assertEquals(savedDatum.getPrice(), datum.price);
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
        log.info("Generating {} Historical records for {} Kryptos took {} ms, performing {}ops/ms", kryptos, kryptos, elapsed, opsPorMillis);
    }

    @Test
    public void deniedPurchaseTest_insufficientBalance() {
        Account u = modelTestUtils.createAccount();
        Krypto k = modelTestUtils.getKrypto(BTC);
        when(bankService.getUserBalance(any())).thenReturn(new UserBalance(u.getId(), new BigDecimal("0")));

        PurchaseRequestDTO purchaseRequest = new PurchaseRequestDTO();
        purchaseRequest.amount = new BigDecimal("0.012001023");
        purchaseRequest.symbol = BTC;

        assertThrows(RuntimeException.class, () -> marketService.buy(u.getId(), purchaseRequest));
    }

    @Test
    public void purchaseTest_permitted() {
        Account a = modelTestUtils.createAccount();
        Krypto k = modelTestUtils.getKrypto(BTC);
        when(bankService.getUserBalance(anyLong())).thenReturn(new UserBalance(a.getId(), new BigDecimal("10000")));

        PurchaseRequestDTO purchaseRequest = new PurchaseRequestDTO();
        purchaseRequest.amount = new BigDecimal("0.012001023");
        purchaseRequest.symbol = BTC;

        TransactionDTO response = marketService.buy(a.getId(), purchaseRequest);
        assertNotNull(response.id);
    }

}
