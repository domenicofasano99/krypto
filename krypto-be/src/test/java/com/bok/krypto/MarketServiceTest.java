package com.bok.krypto;

import com.bok.bank.integration.grpc.AuthorizationResponse;
import com.bok.bank.integration.grpc.Currency;
import com.bok.bank.integration.grpc.Money;
import com.bok.krypto.integration.internal.dto.*;
import com.bok.krypto.model.*;
import com.bok.krypto.repository.HistoricalDataRepository;
import com.bok.krypto.repository.KryptoRepository;
import com.bok.krypto.repository.TransactionRepository;
import com.bok.krypto.service.bank.BankService;
import com.bok.krypto.service.interfaces.MarketService;
import com.bok.krypto.utils.ModelTestUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static com.bok.krypto.utils.Constants.BTC;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
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

    @Test //if it doesn't pass try running it alone, message timing problems...
    public void purchaseTest() throws InterruptedException {
        Account account = modelTestUtils.createAccount();
        Krypto krypto = modelTestUtils.getKrypto(BTC);
        Wallet wallet = modelTestUtils.createWallet(account, krypto, new BigDecimal("0.9"));

        when(bankService.authorize(any(), any(), any(), any(), any())).thenReturn(AuthorizationResponse.newBuilder().setAuthorized(true).setAuthorizationId(UUID.randomUUID().toString()).build());
        when(bankService.convertMoney(any(), any())).thenReturn(Money.newBuilder().setCurrency(Currency.USD).setAmount(10).build());

        PurchaseRequestDTO purchaseRequestDTO = new PurchaseRequestDTO();
        purchaseRequestDTO.symbol = BTC;
        purchaseRequestDTO.amount = new BigDecimal("10");
        purchaseRequestDTO.currencyCode = "USD";
        purchaseRequestDTO.cardToken = "randomToken";
        ActivityDTO activityDTO = marketService.buy(account.getId(), purchaseRequestDTO);
        modelTestUtils.await();
        Thread.sleep(100);
        Transaction transaction = transactionRepository.findByPublicId(activityDTO.publicId).orElse(null);
        assertNotNull(transaction);
        assertEquals(Transaction.Status.AUTHORIZED, transaction.getStatus());
        assertEquals(transaction.getPublicId(), activityDTO.publicId);
        Thread.sleep(10000);
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
    @Transactional
    public void getKryptoHistoricalData() throws InterruptedException {
        Krypto krypto = modelTestUtils.getRandomKrypto();
        Random random = new Random();
        Collection<HistoricalData> collection = new ArrayList<>();
        for (int c = 0; c < 250; c++) {
            HistoricalData hd = new HistoricalData(krypto, random.nextDouble(), Instant.now().minusSeconds(random.nextInt(99999)));
            collection.add(hd);
        }
        historicalDataRepository.saveAllAndFlush(collection);
        krypto.addHistoricalData(collection);
        kryptoRepository.saveAndFlush(krypto);
        Thread.sleep(1000);

        HistoricalDataDTO response = marketService.getKryptoHistoricalData(krypto.getSymbol(), java.time.Instant.parse("2021-08-10T22:00:00.000Z"), Instant.now());
        assertNotNull(response.history);
        assertThat(response.history.size(), greaterThan(0));
        log.info(String.valueOf(response.history));
        for (RecordDTO datum : response.history) {
            log.info("historical record {}", datum);
            HistoricalData savedDatum = historicalDataRepository.findById(datum.id).get();
            assertEquals(savedDatum.getId(), datum.id);
            assertEquals(savedDatum.getPrice(), datum.price);
        }
    }

    @Test
    public void deniedPurchaseTest_insufficientBalance() {
        Account account = modelTestUtils.createAccount();
        Krypto krypto = modelTestUtils.getKrypto(BTC);
        when(bankService.authorize(any(), any(), any(), any(), any())).thenReturn(AuthorizationResponse.newBuilder().setAuthorized(false).setAuthorizationId(UUID.randomUUID().toString()).build());

        PurchaseRequestDTO purchaseRequest = new PurchaseRequestDTO();
        purchaseRequest.amount = new BigDecimal("0.012001023");
        purchaseRequest.symbol = krypto.getSymbol();
        purchaseRequest.currencyCode = "USD";
        purchaseRequest.cardToken = "token";

        ActivityDTO response = marketService.buy(account.getId(), purchaseRequest);
    }

    @Test
    public void purchaseTest_permitted() {
        Account account = modelTestUtils.createAccount();
        Krypto krypto = modelTestUtils.getKrypto(BTC);
        when(bankService.authorize(any(), any(), any(), any(), any())).thenReturn(AuthorizationResponse.newBuilder().setAuthorized(true).setAuthorizationId(UUID.randomUUID().toString()).build());

        PurchaseRequestDTO purchaseRequest = new PurchaseRequestDTO();
        purchaseRequest.amount = new BigDecimal("0.012001023");
        purchaseRequest.symbol = krypto.getSymbol();
        purchaseRequest.currencyCode = "USD";
        purchaseRequest.cardToken = "token";

        ActivityDTO response = marketService.buy(account.getId(), purchaseRequest);
        assertNotNull(response.publicId);
    }

    //@Ignore
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
    public void testMarketHistory() throws InterruptedException {
        Krypto k = modelTestUtils.getRandomKrypto();
        modelTestUtils.addHistoricalDataForKrypto(k, 100);
        Thread.sleep(100);
        HistoricalDataDTO response = marketService.getKryptoHistoricalData(k.getSymbol(), Instant.now().minusSeconds(999999999), Instant.now());
        assertEquals(100, response.history.size());
    }

    @Test
    public void testKryptoInfoList() {
        KryptoInfosDTO kryptoInfosDTO = marketService.getAllKryptoInfos();
        assertNotNull(kryptoInfosDTO);
        assertTrue(kryptoInfosDTO.kryptos.size() > 0);
    }

}
