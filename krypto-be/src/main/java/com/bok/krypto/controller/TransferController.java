package com.bok.krypto.controller;

import com.bok.krypto.integration.internal.dto.TransferInfoDTO;
import com.bok.krypto.integration.internal.dto.TransferRequestDTO;
import com.bok.krypto.integration.internal.dto.TransferResponseDTO;
import com.bok.krypto.service.interfaces.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/transfer")
public class TransferController {
    @Autowired
    TransferService transferService;

    @GetMapping("/{transferId}")
    public TransferInfoDTO transferInfo(@RequestParam("accountId") Long userId, @PathVariable("transferId") UUID transferId) {
        return transferService.transferInfo(userId, transferId);
    }

    @PostMapping("/send")
    public TransferResponseDTO transfer(@RequestParam("accountId") Long userId, @RequestBody TransferRequestDTO requestDTO) {
        return transferService.transfer(userId, requestDTO);
    }

}
