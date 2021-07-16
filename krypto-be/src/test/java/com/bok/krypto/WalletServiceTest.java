package com.bok.krypto;

import com.bok.krypto.core.AddressGenerator;
import com.bok.krypto.exception.KryptoNotFoundException;
import com.bok.krypto.exception.WalletAlreadyExistsException;
import com.bok.krypto.grpc.client.ParentGrpcClient;
import com.bok.krypto.helper.AccountHelper;
import com.bok.krypto.integration.internal.dto.WalletDeleteRequestDTO;
import com.bok.krypto.integration.internal.dto.WalletDeleteResponseDTO;
import com.bok.krypto.integration.internal.dto.WalletRequestDTO;
import com.bok.krypto.integration.internal.dto.WalletResponseDTO;
import com.bok.krypto.integration.internal.dto.WalletsDTO;
import com.bok.krypto.model.Account;
import com.bok.krypto.model.Krypto;
import com.bok.krypto.model.Wallet;
import com.bok.krypto.repository.WalletRepository;
import com.bok.krypto.service.bank.BankClient;
import com.bok.krypto.service.bank.BankService;
import com.bok.krypto.service.interfaces.WalletService;
import com.bok.krypto.utils.ModelTestUtils;
import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;

import static com.bok.krypto.utils.Constants.BTC;
import static com.bok.krypto.utils.Constants.ETH;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @Autowired
    BankService bankService;

    @Autowired
    AddressGenerator addressGenerator;

    @Mock
    AccountHelper accountHelper;

    @Before
    public void setup() {
        BankClient bankClient = mock(BankClient.class);
        ParentGrpcClient parentGrpcClient = mock(ParentGrpcClient.class);
        when(accountHelper.getEmailByAccountId(anyLong())).thenReturn(faker.internet().emailAddress());
        //Mockito.when(bankClient.authorize(anyLong(), any(AuthorizationRequestDTO)).thenReturn(true);
        ReflectionTestUtils.setField(bankService, "bankClient", bankClient);

    }

    @BeforeEach
    public void initialize() {
        modelTestUtils.clearAll();
        modelTestUtils.createBaseKryptos();
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

}
