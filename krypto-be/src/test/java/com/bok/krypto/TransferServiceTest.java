package com.bok.krypto;

import com.bok.integration.krypto.dto.TransferInfoDTO;
import com.bok.integration.krypto.dto.TransferInfoRequestDTO;
import com.bok.integration.krypto.dto.TransferRequestDTO;
import com.bok.integration.krypto.dto.TransferResponseDTO;
import com.bok.krypto.exception.InsufficientBalanceException;
import com.bok.krypto.helper.TransferHelper;
import com.bok.krypto.model.Account;
import com.bok.krypto.model.Krypto;
import com.bok.krypto.model.Transaction;
import com.bok.krypto.model.Wallet;
import com.bok.krypto.repository.TransactionRepository;
import com.bok.krypto.repository.TransferRepository;
import com.bok.krypto.repository.WalletRepository;
import com.bok.krypto.service.interfaces.TransferService;
import com.bok.krypto.utils.ModelTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static com.bok.krypto.utils.Constants.BTC;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
public class TransferServiceTest {

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

    @BeforeEach
    public void setup() {
        modelTestUtils.clearAll();
        modelTestUtils.createBaseKryptos();
    }

    @Test
    public void transferAllowedBetweenUsers() {
        Account a = modelTestUtils.createAccount();
        Account b = modelTestUtils.createAccount();

        Krypto k = modelTestUtils.getKrypto(BTC);

        Wallet wa = modelTestUtils.createWallet(a, k, BigDecimal.valueOf(100));
        Wallet wb = modelTestUtils.createWallet(b, k, BigDecimal.valueOf(10));

        TransferRequestDTO transferRequestDTO = new TransferRequestDTO();
        transferRequestDTO.source = wa.getPublicId();
        transferRequestDTO.destination = wb.getPublicId();
        transferRequestDTO.symbol = BTC;
        transferRequestDTO.amount = BigDecimal.valueOf(5);

        TransferResponseDTO responseDTO = transferService.transfer(a.getId(), transferRequestDTO);
        modelTestUtils.await();

        Wallet fwa = walletRepository.findById(wa.getId()).get();
        Wallet fwb = walletRepository.findById(wb.getId()).get();

        assertTrue(fwa.getAvailableAmount().compareTo(BigDecimal.valueOf(95)) == 0);
        assertTrue(fwb.getAvailableAmount().compareTo(BigDecimal.valueOf(15)) == 0);

    }


    @Test
    public void transferNotAllowed_InsufficientBalance() {
        Krypto k = modelTestUtils.getKrypto(BTC);
        Account a = modelTestUtils.createAccount();
        Wallet wa = modelTestUtils.createWallet(a, k, BigDecimal.valueOf(1));
        Account b = modelTestUtils.createAccount();
        Wallet wb = modelTestUtils.createWallet(b, k, BigDecimal.valueOf(0));
        TransferRequestDTO transferRequestDTO = new TransferRequestDTO();
        transferRequestDTO.source = wa.getPublicId();
        transferRequestDTO.destination = wb.getPublicId();
        transferRequestDTO.symbol = BTC;
        transferRequestDTO.amount = BigDecimal.valueOf(5);
        assertThrows(InsufficientBalanceException.class, () -> transferService.transfer(a.getId(), transferRequestDTO));

    }

    // FIXME: 18/03/21
    public void getTransferInfo() {
        Krypto k = modelTestUtils.getKrypto(BTC);
        Account a = modelTestUtils.createAccount();
        Wallet wa = modelTestUtils.createWallet(a, k, BigDecimal.valueOf(100));
        Account b = modelTestUtils.createAccount();
        Wallet wb = modelTestUtils.createWallet(b, k, BigDecimal.valueOf(10));
        TransferRequestDTO transferRequestDTO = new TransferRequestDTO();
        transferRequestDTO.symbol = BTC;
        transferRequestDTO.destination = wb.getPublicId();
        transferRequestDTO.amount = BigDecimal.valueOf(5);
        TransferResponseDTO responseDTO = transferService.transfer(a.getId(), transferRequestDTO);
        modelTestUtils.await();
        TransferInfoRequestDTO req = new TransferInfoRequestDTO();
        req.transferId = responseDTO.id;
        TransferInfoDTO info = transferService.transferInfo(a.getId(), responseDTO.id);
        assertEquals(Transaction.Status.SETTLED.name(), info.status);
        assertEquals(info.id, responseDTO.id);
    }


}
