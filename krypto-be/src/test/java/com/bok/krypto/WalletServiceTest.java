package com.bok.krypto;

import com.bok.bank.integration.grpc.AuthorizationResponse;
import com.bok.krypto.core.AddressGenerator;
import com.bok.krypto.exception.KryptoNotFoundException;
import com.bok.krypto.exception.WalletAlreadyExistsException;
import com.bok.krypto.helper.AccountHelper;
import com.bok.krypto.integration.internal.dto.*;
import com.bok.krypto.model.*;
import com.bok.krypto.repository.TransactionRepository;
import com.bok.krypto.repository.TransferRepository;
import com.bok.krypto.repository.WalletRepository;
import com.bok.krypto.service.bank.BankService;
import com.bok.krypto.service.interfaces.WalletService;
import com.bok.krypto.service.parent.ParentService;
import com.bok.krypto.utils.ModelTestUtils;
import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.bok.krypto.utils.Constants.BTC;
import static com.bok.krypto.utils.Constants.ETH;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@Slf4j
@ActiveProfiles("test")
public class WalletServiceTest {

    final Faker faker = new Faker();

    @Autowired
    ModelTestUtils modelTestUtils;

    @Autowired
    WalletService walletService;

    @Autowired
    WalletRepository walletRepository;

    @Mock
    BankService bankService;

    @Autowired
    AddressGenerator addressGenerator;

    @Autowired
    AccountHelper accountHelper;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    TransferRepository transferRepository;


    @BeforeEach
    public void initialize() {
        modelTestUtils.clearAll();
        modelTestUtils.createBaseKryptos();


        ParentService parentService = mock(ParentService.class);
        Mockito.when(parentService.getEmailByAccountId(anyLong())).thenReturn(faker.internet().emailAddress());
        ReflectionTestUtils.setField(accountHelper, "parentService", parentService);

        //BankService bankService = mock(BankService.class);
        AuthorizationResponse.Builder authResponse = AuthorizationResponse.newBuilder().setAuthorizationId(UUID.randomUUID().toString()).setAuthorized(true);
        Mockito.when(bankService.authorize(any(), any(), any(), any())).thenReturn(authResponse.build());

        // ReflectionTestUtils.setField(this.bankService, "bankService", bankService);

    }

    @Test
    public void createWalletSuccessful() {

        Account u = modelTestUtils.createAccount();
        Krypto k = modelTestUtils.getRandomKrypto();

        WalletRequestDTO requestDTO = new WalletRequestDTO();
        requestDTO.symbol = k.getSymbol();
        WalletResponseDTO responseDTO = walletService.create(u.getId(), requestDTO);
        modelTestUtils.await();
        assertNotNull(responseDTO);

    }

    @Test
    public void createWalletFail_NoSuchKrypto() {
        Account u = modelTestUtils.createAccount();
        WalletRequestDTO walletRequest = new WalletRequestDTO();
        walletRequest.symbol = "JFK";
        assertThrows(KryptoNotFoundException.class, () -> walletService.create(u.getId(), walletRequest));
        modelTestUtils.await();
    }

    @Test
    public void createWallet_ErrorWalletAlreadyExists() {

        Account account = modelTestUtils.createAccount();
        Krypto k = modelTestUtils.getRandomKrypto();
        Wallet w = modelTestUtils.createWallet(account, k, BigDecimal.TEN);

        WalletRequestDTO requestDTO = new WalletRequestDTO();
        requestDTO.symbol = k.getSymbol();
        assertThrows(WalletAlreadyExistsException.class, () -> walletService.create(account.getId(), requestDTO));

    }

    @Test
    public void deleteWallet_ok() {
        when(accountHelper.getEmailByAccountId(anyLong())).thenReturn(faker.internet().emailAddress());
        Account u = modelTestUtils.createAccount();
        Krypto k = modelTestUtils.getRandomKrypto();
        Wallet w = modelTestUtils.createWallet(u, k, BigDecimal.TEN);

        WalletDeleteRequestDTO deleteRequestDTO = new WalletDeleteRequestDTO();
        deleteRequestDTO.symbol = k.getSymbol();
        WalletDeleteResponseDTO response = walletService.delete(u.getId(), deleteRequestDTO);
        assertNotNull(response);
    }

    @Test
    public void deleteWallet_walletDoesNotExist() {
        Account u = modelTestUtils.createAccount();
        Krypto k = modelTestUtils.getRandomKrypto();

        WalletDeleteRequestDTO deleteRequestDTO = new WalletDeleteRequestDTO();
        deleteRequestDTO.symbol = k.getSymbol();
        assertThrows(RuntimeException.class, () -> walletService.delete(u.getId(), deleteRequestDTO));
    }


    @Test
    public void getAllWallets() {
        Account u = modelTestUtils.createAccount();
        Krypto btc = modelTestUtils.getKrypto(BTC);
        Krypto eth = modelTestUtils.getKrypto(ETH);
        Krypto k = modelTestUtils.getRandomKrypto();
        Wallet wa = modelTestUtils.createWallet(u, btc, BigDecimal.TEN);
        Wallet wb = modelTestUtils.createWallet(u, eth, BigDecimal.ZERO);
        Wallet wc = modelTestUtils.createWallet(u, k, BigDecimal.ZERO);

        WalletsDTO response = walletService.listWallets(u.getId());
        assertNotNull(response);
        assertEquals(response.wallets.size(), 3);
    }


    @Test
    public void createWalletAndThenGetList() {
        Account u = modelTestUtils.createAccount();
        Krypto k = modelTestUtils.getRandomKrypto();

        WalletRequestDTO requestDTO = new WalletRequestDTO();
        requestDTO.symbol = k.getSymbol();
        WalletResponseDTO responseDTO = walletService.create(u.getId(), requestDTO);
        modelTestUtils.await();
        WalletsDTO response = walletService.listWallets(u.getId());

        assertEquals(1, response.wallets.size());
        assertEquals(k.getSymbol(), response.wallets.get(0).symbol);

    }

    @Test
    public void generateWalletAddress() {
        String address = addressGenerator.generateBitcoinAddress();
        assertNotNull(address);
    }

    @Test
    public void testWalletInfo() {

        Account u = modelTestUtils.createAccount();
        Krypto k = modelTestUtils.getRandomKrypto();

        WalletRequestDTO requestDTO = new WalletRequestDTO();
        requestDTO.symbol = k.getSymbol();
        WalletResponseDTO responseDTO = walletService.create(u.getId(), requestDTO);
        modelTestUtils.await();

        WalletInfoDTO response = walletService.info(u.getId(), k.getSymbol(), Instant.now(), Instant.now());
        assertEquals(response.activities.size(), 0);

        Wallet w = walletRepository.findByAccount_IdAndKrypto_Symbol(u.getId(), k.getSymbol()).get();

        Transaction t = new Transaction();
        t.setAccount(u);
        t.setType(Transaction.Type.PURCHASE);
        t.setAmount(BigDecimal.ONE);
        t.setStatus(Activity.Status.SETTLED);
        t.setPublicId(UUID.randomUUID());
        t.setWallet(w);
        t.setFee(0d);
        List<Transaction> transactions = Collections.singletonList(t);
        transactionRepository.saveAll(transactions);

        w.setTransactions(transactions);
        w = walletRepository.save(w);

        response = walletService.info(u.getId(), k.getSymbol(), Instant.now().minus(100, ChronoUnit.DAYS), Instant.now());
        assertEquals(w.getKrypto().getSymbol(), response.symbol);
        assertEquals(response.activities.size(), 1);
    }

}
