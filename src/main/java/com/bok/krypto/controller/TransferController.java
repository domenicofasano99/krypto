package com.bok.krypto.controller;

import com.bok.integration.krypto.dto.TransferInfoDTO;
import com.bok.integration.krypto.dto.TransferRequestDTO;
import com.bok.integration.krypto.dto.TransferResponseDTO;
import com.bok.krypto.service.interfaces.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transfer")
public class TransferController {
    @Autowired
    TransferService transferService;

    @GetMapping("/{userId}/{transferId}")
    TransferInfoDTO transferInfo(@PathVariable("userId") Long userId, @PathVariable("transferId") Long transferId) {
        return transferService.transferInfo(userId, transferId);
    }

    @PostMapping("/{userId}")
    TransferResponseDTO transfer(@PathVariable("userId") Long userId, TransferRequestDTO requestDTO) {
        return transferService.transfer(userId, requestDTO);
    }

}
