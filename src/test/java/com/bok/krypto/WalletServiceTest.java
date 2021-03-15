package com.bok.krypto;

import com.bok.integration.krypto.dto.WalletRequestDTO;
import com.bok.integration.krypto.dto.WalletResponseDTO;
import com.bok.krypto.exception.KryptoNotFoundException;
import com.bok.krypto.exception.WalletAlreadyExistsException;
import com.bok.krypto.model.Krypto;
import com.bok.krypto.model.User;
import com.bok.krypto.repository.WalletRepository;
import com.bok.krypto.service.interfaces.WalletService;
import com.bok.krypto.utils.ModelTestUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

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

        User u = modelTestUtils.createUser();
        Krypto k = modelTestUtils.getRandomKrypto();
        WalletRequestDTO requestDTO = new WalletRequestDTO();
        requestDTO.userId = u.getId();
        requestDTO.symbol = k.getSymbol();
        WalletResponseDTO responseDTO = walletService.create(requestDTO);
        assertNotNull(responseDTO);
        assertNotNull(responseDTO.id);
        assertNotNull(responseDTO.creationTime);

    }

    @Test
    public void createWalletFail_NoSuchKrypto() {
        User u = modelTestUtils.createUser();
        assertThrows(KryptoNotFoundException.class, () -> modelTestUtils.createWallet(u, "GGG"));
    }

    @Test
    public void createWallet_ErrorWalletAlreadyExists() {

        User u = modelTestUtils.createUser();
        Krypto k = modelTestUtils.getRandomKrypto();
        WalletRequestDTO requestDTO = new WalletRequestDTO();
        requestDTO.userId = u.getId();
        requestDTO.symbol = k.getSymbol();
        WalletResponseDTO responseDTO = walletService.create(requestDTO);
        assertNotNull(responseDTO);
        assertThrows(WalletAlreadyExistsException.class, () -> walletService.create(requestDTO));

    }
}
