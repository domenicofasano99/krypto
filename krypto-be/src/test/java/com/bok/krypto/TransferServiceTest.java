package com.bok.krypto;

import com.bok.krypto.exception.TransactionException;
import com.bok.krypto.helper.AccountHelper;
import com.bok.krypto.helper.TransferHelper;
import com.bok.krypto.integration.internal.dto.TransferInfoDTO;
import com.bok.krypto.integration.internal.dto.TransferInfoRequestDTO;
import com.bok.krypto.integration.internal.dto.TransferRequestDTO;
import com.bok.krypto.integration.internal.dto.TransferResponseDTO;
import com.bok.krypto.model.Account;
import com.bok.krypto.model.Krypto;
import com.bok.krypto.model.Transfer;
import com.bok.krypto.model.Wallet;
import com.bok.krypto.repository.TransactionRepository;
import com.bok.krypto.repository.TransferRepository;
import com.bok.krypto.repository.WalletRepository;
import com.bok.krypto.service.interfaces.TransferService;
import com.bok.krypto.service.parent.ParentService;
import com.bok.krypto.utils.ModelTestUtils;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;

import static com.bok.krypto.model.Activity.Status.SETTLED;
import static com.bok.krypto.util.Constants.STANDARD_CURRENCY;
import static com.bok.krypto.utils.Constants.BTC;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;

@SpringBootTest
@ActiveProfiles("test")
public class TransferServiceTest {

    final Faker faker = new Faker();
    @Autowired
    ModelTestUtils modelTestUtils;
    @Autowired
    TransferService transferService;
    @Autowired
    WalletRepository walletRepository;
    @Autowired
    TransferHelper transferHelper;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    TransferRepository transferRepository;
    @Autowired
    AccountHelper accountHelper;

    @BeforeEach
    public void setup() {
        modelTestUtils.clearAll();
        modelTestUtils.createBaseKryptos();

        ParentService parentService = mock(ParentService.class);
        Mockito.when(parentService.getEmailByAccountId(anyLong())).thenReturn(faker.internet().emailAddress());
        ReflectionTestUtils.setField(accountHelper, "parentService", parentService);
    }

    @Test
    public void transferAllowedBetweenWallets() {
        Account a = modelTestUtils.createAccount();
        Account b = modelTestUtils.createAccount();

        Krypto k = modelTestUtils.getKrypto(BTC);

        Wallet wa = modelTestUtils.createWallet(a, k, BigDecimal.valueOf(100));
        Wallet wb = modelTestUtils.createWallet(b, k, BigDecimal.valueOf(10));

        TransferRequestDTO transferRequestDTO = new TransferRequestDTO();
        transferRequestDTO.source = wa.getAddress();
        transferRequestDTO.destination = wb.getAddress();
        transferRequestDTO.symbol = BTC;
        transferRequestDTO.amount = BigDecimal.valueOf(5);
        transferRequestDTO.currencyCode = "USD";

        TransferResponseDTO responseDTO = transferService.transfer(a.getId(), transferRequestDTO);
        modelTestUtils.await();
        Wallet fwa = walletRepository.findById(wa.getId()).get();
        Wallet fwb = walletRepository.findById(wb.getId()).get();
        Transfer t = transferRepository.findByPublicId(responseDTO.publicId).orElseThrow(RuntimeException::new);
        assertEquals(SETTLED, t.getStatus());

    }

    @Test
    public void testMalformedTransfer() {
        Account a = modelTestUtils.createAccount();
        Account b = modelTestUtils.createAccount();

        Krypto k = modelTestUtils.getKrypto(BTC);

        Wallet wa = modelTestUtils.createWallet(a, k, BigDecimal.TEN);
        Wallet wb = modelTestUtils.createWallet(b, k, BigDecimal.TEN);


        TransferRequestDTO transferRequestDTO = new TransferRequestDTO();
        transferRequestDTO.source = wa.getAddress();
        transferRequestDTO.destination = wb.getAddress();
        transferRequestDTO.symbol = BTC;
        transferRequestDTO.amount = BigDecimal.valueOf(-5);

        assertThrows(RuntimeException.class, () -> transferService.transfer(a.getId(), transferRequestDTO));
    }


    @Test
    public void transferNotAllowed_InsufficientBalance() {
        Krypto k = modelTestUtils.getKrypto(BTC);
        Account a = modelTestUtils.createAccount();
        Wallet wa = modelTestUtils.createWallet(a, k, BigDecimal.valueOf(1));
        Account b = modelTestUtils.createAccount();
        Wallet wb = modelTestUtils.createWallet(b, k, BigDecimal.valueOf(0));
        TransferRequestDTO transferRequestDTO = new TransferRequestDTO();
        transferRequestDTO.source = wa.getAddress();
        transferRequestDTO.destination = wb.getAddress();
        transferRequestDTO.symbol = BTC;
        transferRequestDTO.amount = BigDecimal.valueOf(5000000);
        transferRequestDTO.currencyCode = "USD";
        assertThrows(TransactionException.class, () -> transferService.transfer(a.getId(), transferRequestDTO));

    }

    @Test
    public void getTransferInfo() {
        Krypto k = modelTestUtils.getKrypto(BTC);
        Account a = modelTestUtils.createAccount();
        Wallet wa = modelTestUtils.createWallet(a, k, BigDecimal.valueOf(100));
        Account b = modelTestUtils.createAccount();
        Wallet wb = modelTestUtils.createWallet(b, k, BigDecimal.valueOf(10));
        TransferRequestDTO transferRequestDTO = new TransferRequestDTO();
        transferRequestDTO.symbol = BTC;
        transferRequestDTO.source = wa.getAddress();
        transferRequestDTO.destination = wb.getAddress();
        transferRequestDTO.amount = BigDecimal.valueOf(5);
        transferRequestDTO.currencyCode = STANDARD_CURRENCY.getCurrencyCode();
        TransferResponseDTO responseDTO = transferService.transfer(a.getId(), transferRequestDTO);
        modelTestUtils.await();

        TransferInfoRequestDTO req = new TransferInfoRequestDTO();
        req.transferId = responseDTO.publicId;
        TransferInfoDTO info = transferService.transferInfo(a.getId(), responseDTO.publicId);
        assertEquals(SETTLED.name(), info.status);
        assertEquals(info.publicId, responseDTO.publicId);
    }

    @Test
    public void tryTransferToUnknownAccount() {
        Krypto k = modelTestUtils.getKrypto(BTC);
        Account a = modelTestUtils.createAccount();
        Wallet wa = modelTestUtils.createWallet(a, k, BigDecimal.valueOf(100));

        TransferRequestDTO transferRequestDTO = new TransferRequestDTO();
        transferRequestDTO.symbol = BTC;
        transferRequestDTO.source = wa.getAddress();
        transferRequestDTO.destination = "1BvBMSEYstWetqTFn5Au4m4GFg7xJaNVN2";
        transferRequestDTO.amount = BigDecimal.valueOf(5);
        transferRequestDTO.currencyCode = STANDARD_CURRENCY.getCurrencyCode();
        assertThrows(RuntimeException.class, () -> transferService.transfer(a.getId(), transferRequestDTO));
    }

}
