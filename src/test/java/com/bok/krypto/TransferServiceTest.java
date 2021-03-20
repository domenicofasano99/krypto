package com.bok.krypto;

import com.bok.integration.krypto.dto.TransferInfoDTO;
import com.bok.integration.krypto.dto.TransferInfoRequestDTO;
import com.bok.integration.krypto.dto.TransferRequestDTO;
import com.bok.integration.krypto.dto.TransferResponseDTO;
import com.bok.krypto.exception.InsufficientBalanceException;
import com.bok.krypto.helper.TransferHelper;
import com.bok.krypto.model.Krypto;
import com.bok.krypto.model.Transaction;
import com.bok.krypto.model.User;
import com.bok.krypto.model.Wallet;
import com.bok.krypto.repository.TransactionRepository;
import com.bok.krypto.repository.TransferRepository;
import com.bok.krypto.repository.WalletRepository;
import com.bok.krypto.service.interfaces.TransferService;
import com.bok.krypto.utils.ModelTestUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static com.bok.krypto.utils.Constants.BTC;
import static org.junit.Assert.*;

@SpringBootTest
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

    @BeforeAll
    public static void setupMocks() {
        //setup mocks here
    }

    @BeforeEach
    public void setup() {
        modelTestUtils.clearAll();
        modelTestUtils.populateDB();
    }

    @Test
    public void transferAllowedBetweenUsers() {
        User a = modelTestUtils.createUser();
        Krypto k = modelTestUtils.getKrypto(BTC);
        Wallet wa = modelTestUtils.createWallet(a, k, new BigDecimal(100));
        User b = modelTestUtils.createUser();
        Wallet wb = modelTestUtils.createWallet(b, k, new BigDecimal(10));

        TransferRequestDTO transferRequestDTO = new TransferRequestDTO();
        transferRequestDTO.symbol = BTC;
        transferRequestDTO.destination = wb.getId();
        transferRequestDTO.amount = new BigDecimal(5);
        TransferResponseDTO responseDTO = transferService.transfer(a.getId(), transferRequestDTO);
        modelTestUtils.await();

        Wallet fwa = walletRepository.findById(wa.getId()).get();
        Wallet fwb = walletRepository.findById(wb.getId()).get();

        assertTrue(fwa.getAvailableAmount().compareTo(new BigDecimal(95)) == 0);
        assertTrue(fwb.getAvailableAmount().compareTo(new BigDecimal(15)) == 0);

    }


    @Test
    public void transferNotAllowed_InsufficientBalance() {
        Krypto k = modelTestUtils.getKrypto(BTC);
        User a = modelTestUtils.createUser();
        Wallet wa = modelTestUtils.createWallet(a, k, new BigDecimal(1));
        User b = modelTestUtils.createUser();
        Wallet wb = modelTestUtils.createWallet(b, k, new BigDecimal(0));
        TransferRequestDTO transferRequestDTO = new TransferRequestDTO();
        transferRequestDTO.symbol = BTC;
        transferRequestDTO.destination = wb.getId();
        transferRequestDTO.amount = new BigDecimal(5);
        assertThrows(InsufficientBalanceException.class, () -> transferService.transfer(a.getId(), transferRequestDTO));

    }

    // FIXME: 18/03/21
    public void getTransferInfo() {
        Krypto k = modelTestUtils.getKrypto(BTC);
        User a = modelTestUtils.createUser();
        Wallet wa = modelTestUtils.createWallet(a, k, new BigDecimal(100));
        User b = modelTestUtils.createUser();
        Wallet wb = modelTestUtils.createWallet(b, k, new BigDecimal(10));
        TransferRequestDTO transferRequestDTO = new TransferRequestDTO();
        transferRequestDTO.symbol = BTC;
        transferRequestDTO.destination = wb.getId();
        transferRequestDTO.amount = new BigDecimal(5);
        TransferResponseDTO responseDTO = transferService.transfer(a.getId(), transferRequestDTO);
        modelTestUtils.await();
        TransferInfoRequestDTO req = new TransferInfoRequestDTO();
        req.transferId = responseDTO.id;
        TransferInfoDTO info = transferService.transferInfo(a.getId(), req);
        assertEquals(Transaction.Status.SETTLED.name(), info.status);
        assertEquals(info.id, responseDTO.id);
    }


}
