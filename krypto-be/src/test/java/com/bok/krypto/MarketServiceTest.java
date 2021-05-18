package com.bok.krypto;

import com.bok.bank.integration.dto.AuthorizationResponseDTO;
import com.bok.krypto.integration.internal.dto.HistoricalDataDTO;
import com.bok.krypto.integration.internal.dto.HistoricalDataRequestDTO;
import com.bok.krypto.integration.internal.dto.KryptoInfoDTO;
import com.bok.krypto.integration.internal.dto.KryptoInfosDTO;
import com.bok.krypto.integration.internal.dto.KryptoInfosRequestDTO;
import com.bok.krypto.integration.internal.dto.PriceResponseDTO;
import com.bok.krypto.integration.internal.dto.PurchaseRequestDTO;
import com.bok.krypto.integration.internal.dto.RecordDTO;
import com.bok.krypto.integration.internal.dto.TransactionDTO;
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
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import static com.bok.krypto.utils.Constants.BTC;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@Slf4j
@ActiveProfiles("test")
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
        modelTestUtils.createBaseKryptos();
    }

    @Test //if it doesnt pass try running it alone, message timing problems...
    public void purchaseTest() {
        Account account = modelTestUtils.createAccount();
        Krypto krypto = modelTestUtils.getKrypto(BTC);
        Wallet wallet = modelTestUtils.createWallet(account, krypto, new BigDecimal("0.9"));
        when(bankService.preauthorize(any(), any(), any())).thenReturn(new AuthorizationResponseDTO(true, "", 1L));
        PurchaseRequestDTO purchaseRequestDTO = new PurchaseRequestDTO();
        purchaseRequestDTO.symbol = BTC;
        purchaseRequestDTO.amount = new BigDecimal("0.8989827");
        TransactionDTO transactionDTO = marketService.buy(account.getId(), purchaseRequestDTO);
        modelTestUtils.await();
        Transaction transaction = transactionRepository.findByPublicId(transactionDTO.publicId).orElse(null);
        assertNotNull(transaction);
        assertEquals(transaction.getPublicId(), transactionDTO.publicId);
        assertEquals(Transaction.Status.SETTLED, transaction.getStatus());

    }

    @Test
    public void testPurchaseMessage() {
        //test purchase by sending a message to the processor
    }

    @Test
    public void getKryptoPrice() {

        Account account = modelTestUtils.createAccount();
        List<Krypto> list = kryptoRepository.findAll();

        Krypto krypto = list.get(0);

        PriceResponseDTO responseDTO = marketService.getKryptoPrice(krypto.getSymbol());

        assertNotNull(responseDTO);
        assertNotNull(responseDTO.price);
        assertThat(responseDTO.price, is(krypto.getPrice()));

    }

    @Test
    public void getKryptoInfo() {
        Account account = modelTestUtils.createAccount();
        Krypto krypto = modelTestUtils.getRandomKrypto();
        KryptoInfoDTO responseDTO = marketService.getKryptoInfo(krypto.getSymbol());
        assertNotNull(responseDTO);
        assertEquals(responseDTO.price, krypto.getPrice());
        assertEquals(responseDTO.name, krypto.getName());
        assertEquals(responseDTO.networkFee, krypto.getNetworkFee());
        assertEquals(responseDTO.updateTimestamp, krypto.getUpdateTimestamp());
    }

    @Test
    public void getKryptoInfos() {
        Account account = modelTestUtils.createAccount();
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
        Account account = modelTestUtils.createAccount();
        Krypto krypto = modelTestUtils.getRandomKrypto();

        HistoricalDataRequestDTO requestDTO = new HistoricalDataRequestDTO();
        requestDTO.start = Instant.parse("2020-12-03T10:15:30.00Z");
        requestDTO.end = Instant.now();
        requestDTO.symbol = krypto.getSymbol();
        HistoricalDataDTO response = marketService.getKryptoHistoricalData(requestDTO);
        assertNotNull(response.history);
        log.info(String.valueOf(response.history));
        for (RecordDTO datum : response.history) {
            HistoricalData savedDatum = historicalDataRepository.findById(datum.id).get();
            assertEquals(savedDatum.getId(), datum.id);
            assertEquals(savedDatum.getPrice(), datum.price);
        }
    }

    @Test
    public void deniedPurchaseTest_insufficientBalance() {
        Account account = modelTestUtils.createAccount();
        Krypto krypto = modelTestUtils.getKrypto(BTC);
        when(bankService.preauthorize(any(), any(), any())).thenReturn(new AuthorizationResponseDTO(false, "", -1L));

        PurchaseRequestDTO purchaseRequest = new PurchaseRequestDTO();
        purchaseRequest.amount = new BigDecimal("0.012001023");
        purchaseRequest.symbol = BTC;

        TransactionDTO response = marketService.buy(account.getId(), purchaseRequest);
    }

    @Test
    public void purchaseTest_permitted() {
        Account account = modelTestUtils.createAccount();
        Krypto modelTestUtilsKrypto = modelTestUtils.getKrypto(BTC);
        when(bankService.preauthorize(any(), any(), any())).thenReturn(new AuthorizationResponseDTO(true, "", 1L));

        PurchaseRequestDTO purchaseRequest = new PurchaseRequestDTO();
        purchaseRequest.amount = new BigDecimal("0.012001023");
        purchaseRequest.symbol = BTC;

        TransactionDTO response = marketService.buy(account.getId(), purchaseRequest);
        assertNotNull(response.publicId);
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

}
