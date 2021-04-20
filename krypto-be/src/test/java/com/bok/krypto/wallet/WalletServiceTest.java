package com.bok.krypto.wallet;

import com.bok.integration.krypto.WalletDeleteRequestDTO;
import com.bok.integration.krypto.WalletDeleteResponseDTO;
import com.bok.integration.krypto.WalletsDTO;
import com.bok.integration.krypto.dto.WalletRequestDTO;
import com.bok.integration.krypto.dto.WalletResponseDTO;
import com.bok.krypto.exception.KryptoNotFoundException;
import com.bok.krypto.exception.WalletAlreadyExistsException;
import com.bok.krypto.model.Account;
import com.bok.krypto.model.Krypto;
import com.bok.krypto.model.Wallet;
import com.bok.krypto.repository.WalletRepository;
import com.bok.krypto.service.interfaces.WalletService;
import com.bok.krypto.utils.ModelTestUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static com.bok.krypto.utils.Constants.BTC;
import static com.bok.krypto.utils.Constants.ETH;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
public class WalletServiceTest {

    @Autowired
    ModelTestUtils modelTestUtils;

    @Autowired
    WalletService walletService;

    @Autowired
    WalletRepository walletRepository;

    @BeforeEach
    public void configureTests() {
        modelTestUtils.clearAll();
        log.info("DB dropped correctly.");
        modelTestUtils.populateDB();
        log.info("DB populated correctly.");
    }

    @Test
    public void createWalletSuccessful() {

        Account u = modelTestUtils.createUser();
        Krypto k = modelTestUtils.getRandomKrypto();
        WalletRequestDTO requestDTO = new WalletRequestDTO();
        requestDTO.symbol = k.getSymbol();
        WalletResponseDTO responseDTO = walletService.create(u.getId(), requestDTO);
        modelTestUtils.await();
        assertNotNull(responseDTO);

    }

    @Test
    public void createWalletFail_NoSuchKrypto() {
        Account u = modelTestUtils.createUser();
        WalletRequestDTO walletRequest = new WalletRequestDTO();
        walletRequest.symbol = "JFK";
        assertThrows(KryptoNotFoundException.class, () -> walletService.create(u.getId(), walletRequest));
        modelTestUtils.await();
    }

    @Test
    public void createWallet_ErrorWalletAlreadyExists() {

        Account u = modelTestUtils.createUser();
        Krypto k = modelTestUtils.getRandomKrypto();
        WalletRequestDTO requestDTO = new WalletRequestDTO();

        requestDTO.symbol = k.getSymbol();
        WalletResponseDTO responseDTO = walletService.create(u.getId(), requestDTO);
        assertNotNull(responseDTO);
        modelTestUtils.await();
        assertThrows(WalletAlreadyExistsException.class, () -> walletService.create(u.getId(), requestDTO));

    }

    @Test
    public void deleteWallet_ok() {
        Account u = modelTestUtils.createUser();
        Krypto k = modelTestUtils.getRandomKrypto();
        Wallet w = modelTestUtils.createWallet(u, k, BigDecimal.TEN);

        WalletDeleteRequestDTO deleteRequestDTO = new WalletDeleteRequestDTO();
        deleteRequestDTO.symbol = k.getSymbol();
        deleteRequestDTO.destinationIBAN = "it003300231872879124298";
        WalletDeleteResponseDTO response = walletService.delete(u.getId(), deleteRequestDTO);
        assertNotNull(response);
    }

    @Test
    public void deleteWallet_walletDoesNotExist() {
        Account u = modelTestUtils.createUser();
        Krypto k = modelTestUtils.getRandomKrypto();

        WalletDeleteRequestDTO deleteRequestDTO = new WalletDeleteRequestDTO();
        deleteRequestDTO.symbol = k.getSymbol();
        deleteRequestDTO.destinationIBAN = "it003300231872879124298";
        assertThrows(RuntimeException.class, () -> walletService.delete(u.getId(), deleteRequestDTO));
    }

    @Test
    public void deleteWallet_IBAN_not_given() {
        Account u = modelTestUtils.createUser();
        Krypto k = modelTestUtils.getRandomKrypto();
        Wallet w = modelTestUtils.createWallet(u, k, BigDecimal.TEN);

        WalletDeleteRequestDTO deleteRequestDTO = new WalletDeleteRequestDTO();
        deleteRequestDTO.symbol = k.getSymbol();
        deleteRequestDTO.destinationIBAN = null;
        assertThrows(RuntimeException.class, () -> walletService.delete(u.getId(), deleteRequestDTO));
    }


    @Test
    public void getAllWallets() {
        Account u = modelTestUtils.createUser();
        Krypto btc = modelTestUtils.getKrypto(BTC);
        Krypto eth = modelTestUtils.getKrypto(ETH);
        Wallet wa = modelTestUtils.createWallet(u, btc, BigDecimal.TEN);
        Wallet wb = modelTestUtils.createWallet(u, eth, BigDecimal.ZERO);

        WalletsDTO response = walletService.wallets(u.getId());
        assertNotNull(response);
        assertEquals(response.wallets.size(), 2);

    }


}
