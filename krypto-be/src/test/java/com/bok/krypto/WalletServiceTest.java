package com.bok.krypto;

import com.bok.bank.integration.grpc.AuthorizationResponse;
import com.bok.krypto.core.AddressGenerator;
import com.bok.krypto.exception.KryptoNotFoundException;
import com.bok.krypto.exception.WalletAlreadyExistsException;
import com.bok.krypto.helper.AccountHelper;
import com.bok.krypto.helper.WalletHelper;
import com.bok.krypto.integration.internal.dto.ValidationRequestDTO;
import com.bok.krypto.integration.internal.dto.WalletDeleteRequestDTO;
import com.bok.krypto.integration.internal.dto.WalletDeleteResponseDTO;
import com.bok.krypto.integration.internal.dto.WalletInfoDTO;
import com.bok.krypto.integration.internal.dto.WalletRequestDTO;
import com.bok.krypto.integration.internal.dto.WalletResponseDTO;
import com.bok.krypto.integration.internal.dto.WalletsDTO;
import com.bok.krypto.model.Account;
import com.bok.krypto.model.Activity;
import com.bok.krypto.model.BalanceSnapshot;
import com.bok.krypto.model.Krypto;
import com.bok.krypto.model.Transaction;
import com.bok.krypto.model.Wallet;
import com.bok.krypto.repository.BalanceSnapshotRepository;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.bok.krypto.utils.Constants.BTC;
import static com.bok.krypto.utils.Constants.ETH;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

    @Autowired
    BalanceSnapshotRepository balanceSnapshotRepository;

    @Autowired
    WalletHelper walletHelper;


    @BeforeEach
    public void initialize() {
        modelTestUtils.clearAll();
        modelTestUtils.createBaseKryptos();

        ParentService parentService = mock(ParentService.class);
        Mockito.when(parentService.getEmailByAccountId(anyLong())).thenReturn(faker.internet().emailAddress());
        ReflectionTestUtils.setField(accountHelper, "parentService", parentService);

        AuthorizationResponse.Builder authResponse = AuthorizationResponse.newBuilder().setAuthorizationId(UUID.randomUUID().toString()).setAuthorized(true);
        Mockito.when(bankService.authorize(any(), any(), any(), any(), any())).thenReturn(authResponse.build());

    }

    @Test
    public void createWalletSuccessful() {

        Account u = modelTestUtils.createAccount();
        Krypto k = modelTestUtils.getRandomKrypto();

        WalletRequestDTO requestDTO = new WalletRequestDTO();
        requestDTO.symbol = k.getSymbol();
        WalletResponseDTO responseDTO = walletService.create(u.getId(), requestDTO);
        assertNotNull(responseDTO);
    }

    @Test
    public void testFirstBalanceHistoryOnWalletCreation() {
        Account a = modelTestUtils.createAccount();
        Krypto k = modelTestUtils.getRandomKrypto();
        Wallet w = modelTestUtils.createWallet(a, k, BigDecimal.ZERO);

        List<BalanceSnapshot> balanceSnapshot = balanceSnapshotRepository.findByWallet_Id(w.getId());
        assertEquals(1, balanceSnapshot.size());
        assertEquals(0, BigDecimal.ZERO.compareTo(balanceSnapshot.get(0).getAmount()));
        assertEquals(0, BigDecimal.ZERO.compareTo(balanceSnapshot.get(0).getValue()));
        assertEquals(w, balanceSnapshot.get(0).getWallet());
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
    public void createWalletAndThenGetList() throws InterruptedException {
        Account u = modelTestUtils.createAccount();
        Krypto k = modelTestUtils.getRandomKrypto();

        WalletRequestDTO requestDTO = new WalletRequestDTO();
        requestDTO.symbol = k.getSymbol();
        WalletResponseDTO responseDTO = walletService.create(u.getId(), requestDTO);
        //modelTestUtils.await();
        WalletsDTO response = walletService.listWallets(u.getId());
        Thread.sleep(1000);
        assertEquals(1, response.wallets.size());

    }

    @Test
    public void generateWalletAddress() {
        String address = addressGenerator.generateWalletAddress();
        assertNotNull(address);
    }

    @Test
    public void checkForWalletAddressUniqueness() {
        Set<String> addresses = new HashSet<>();

        for (int c = 0; c < 1000; c++) {
            String address = addressGenerator.generateWalletAddress();
            assertFalse(addresses.contains(address));
            addresses.add(address);
        }
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

        Wallet w = walletRepository.findByAccount_IdAndKrypto_SymbolAndDeletedIsFalse(u.getId(), k.getSymbol()).get();

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

    @Test
    public void testAddressValidation_validAddress() {
        Account a = modelTestUtils.createAccount();
        Krypto k = modelTestUtils.getRandomKrypto();
        Wallet w = modelTestUtils.createWallet(a, k, BigDecimal.ZERO);

        ValidationRequestDTO request = new ValidationRequestDTO();
        request.address = w.getAddress();
        request.symbol = k.getSymbol();
        assertTrue(walletService.validateAddress(request));
    }

    @Test
    public void testAddressValidation_invalidAddress() {
        Krypto k = modelTestUtils.getRandomKrypto();

        ValidationRequestDTO request = new ValidationRequestDTO();
        request.address = "address";
        request.symbol = k.getSymbol();
        assertFalse(walletService.validateAddress(request));
    }

    @Test
    public void balanceHistoryTest() throws InterruptedException {
        Account a = modelTestUtils.createAccount();
        Krypto k = modelTestUtils.getRandomKrypto();
        Wallet w = modelTestUtils.createWallet(a, k, BigDecimal.ZERO);

        walletHelper.deposit(w, BigDecimal.TEN);
        assertEquals(2, balanceSnapshotRepository.findByWallet_Id(w.getId()).size());
        assertEquals(0, BigDecimal.TEN.compareTo(balanceSnapshotRepository.findByWallet_Id(w.getId()).get(1).getAmount()));
        Thread.sleep(100);


        walletHelper.withdraw(w, BigDecimal.ONE);

        WalletInfoDTO wInfo = walletService.info(a.getId(), k.getSymbol(), Instant.now().minus(30, ChronoUnit.DAYS), Instant.now());
        assertEquals(3, wInfo.balanceHistory.size());
        assertEquals(3, balanceSnapshotRepository.findByWallet_Id(w.getId()).size());
    }

    @Test
    public void testWalletDelete() {
        Account a = modelTestUtils.createAccount();
        Krypto k = modelTestUtils.getRandomKrypto();
        Wallet w = modelTestUtils.createWallet(a, k, BigDecimal.ZERO);
        walletHelper.deposit(w, BigDecimal.TEN);

        Transaction t = new Transaction();
        t.setStatus(Activity.Status.SETTLED);
        t.setAccount(a);
        t.setWallet(w);
        t.setPublicId(UUID.randomUUID());
        t.setType(Transaction.Type.PURCHASE);

        transactionRepository.save(t);

        WalletDeleteRequestDTO request = new WalletDeleteRequestDTO();
        request.symbol = w.getKrypto().getSymbol();
        walletService.delete(a.getId(), request);
    }

}
