package com.bok.krypto;

import com.bok.integration.krypto.dto.TransferInfoDTO;
import com.bok.integration.krypto.dto.TransferInfoRequestDTO;
import com.bok.integration.krypto.dto.TransferRequestDTO;
import com.bok.integration.krypto.dto.TransferResponseDTO;
import com.bok.krypto.exception.InsufficientBalanceException;
import com.bok.krypto.model.User;
import com.bok.krypto.model.Wallet;
import com.bok.krypto.repository.WalletRepository;
import com.bok.krypto.service.interfaces.TransferService;
import com.bok.krypto.utils.ModelTestUtils;
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

    @BeforeEach
    public void setup() {
        modelTestUtils.clearAll();
        modelTestUtils.populateDB();
    }

    @Test
    public void transferAllowedBetweenUsers() {
        User a = modelTestUtils.createUser(10L);
        Wallet wa = modelTestUtils.createWallet(a, BTC, new BigDecimal(100));
        User b = modelTestUtils.createUser();
        Wallet wb = modelTestUtils.createWallet(b, BTC, new BigDecimal(10));

        TransferRequestDTO transferRequestDTO = new TransferRequestDTO();
        transferRequestDTO.from = wa.getId();
        transferRequestDTO.destination = wb.getId();
        transferRequestDTO.amount = new BigDecimal(5);
        TransferResponseDTO responseDTO = transferService.transfer(transferRequestDTO);

        Wallet fwa = walletRepository.findById(wa.getId()).get();
        Wallet fwb = walletRepository.findById(wb.getId()).get();
        assertTrue(fwa.getAvailableAmount().compareTo(new BigDecimal(95)) == 0);
        assertTrue(fwb.getAvailableAmount().compareTo(new BigDecimal(15)) == 0);

    }

    @Test
    public void transferNotAllowed_InsufficientBalance() {
        User a = modelTestUtils.createUser(10L);
        Wallet wa = modelTestUtils.createWallet(a, BTC, new BigDecimal(1));
        User b = modelTestUtils.createUser();
        Wallet wb = modelTestUtils.createWallet(b, BTC, new BigDecimal(0));

        TransferRequestDTO transferRequestDTO = new TransferRequestDTO();
        transferRequestDTO.from = wa.getId();
        transferRequestDTO.destination = wb.getId();
        transferRequestDTO.amount = new BigDecimal(5);
        assertThrows(InsufficientBalanceException.class, () -> transferService.transfer(transferRequestDTO));

    }

    @Test
    public void getTransferInfo() {
        User a = modelTestUtils.createUser(10L);
        Wallet wa = modelTestUtils.createWallet(a, BTC, new BigDecimal(100));
        User b = modelTestUtils.createUser();
        Wallet wb = modelTestUtils.createWallet(b, BTC, new BigDecimal(10));

        TransferRequestDTO transferRequestDTO = new TransferRequestDTO();
        transferRequestDTO.from = wa.getId();
        transferRequestDTO.destination = wb.getId();
        transferRequestDTO.amount = new BigDecimal(5);
        TransferResponseDTO responseDTO = transferService.transfer(transferRequestDTO);

        TransferInfoRequestDTO req = new TransferInfoRequestDTO();
        req.transferId = responseDTO.id;
        TransferInfoDTO info = transferService.transferInfo(req);

        assertEquals(info.id, responseDTO.id);
    }
}
