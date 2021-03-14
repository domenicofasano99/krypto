package com.bok.krypto;

import com.bok.krypto.exception.KryptoNotFoundException;
import com.bok.krypto.model.User;
import com.bok.krypto.model.Wallet;
import com.bok.krypto.utils.ModelTestUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static com.bok.krypto.utils.Constants.BTC;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

@SpringBootTest
@Slf4j
public class WalletServiceTest {

    @Autowired
    ModelTestUtils modelTestUtils;

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
        Wallet w = modelTestUtils.createWallet(u, BTC);
        assertNotNull(w);
    }

    @Test
    public void createWalletFail_NoSuchKrypto() {
        User u = modelTestUtils.createUser();
        assertThrows(KryptoNotFoundException.class, () -> modelTestUtils.createWallet(u, "GGG"));
    }
}
