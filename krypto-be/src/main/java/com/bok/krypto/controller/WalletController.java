package com.bok.krypto.controller;

import com.bok.krypto.integration.internal.dto.WalletDeleteRequestDTO;
import com.bok.krypto.integration.internal.dto.WalletDeleteResponseDTO;
import com.bok.krypto.integration.internal.dto.WalletRequestDTO;
import com.bok.krypto.integration.internal.dto.WalletResponseDTO;
import com.bok.krypto.integration.internal.dto.WalletsDTO;
import com.bok.krypto.service.interfaces.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wallet")
public class WalletController {

    @Autowired
    WalletService walletService;

    @PostMapping("/create")
    WalletResponseDTO create(@RequestParam("accountId") Long accountId, @RequestBody WalletRequestDTO requestDTO) {
        return walletService.create(accountId, requestDTO);
    }

    @PostMapping("/delete")
    WalletDeleteResponseDTO delete(@RequestParam("accountId") Long accountId, @RequestBody WalletDeleteRequestDTO requestDTO) {
        return walletService.delete(accountId, requestDTO);
    }

    @GetMapping("/list")
    WalletsDTO wallets(@RequestParam("accountId") Long accountId) {
        return walletService.wallets(accountId);
    }
}
