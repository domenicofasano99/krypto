package com.bok.krypto.controller;

import com.bok.integration.krypto.WalletDeleteRequestDTO;
import com.bok.integration.krypto.WalletDeleteResponseDTO;
import com.bok.integration.krypto.WalletsDTO;
import com.bok.integration.krypto.dto.WalletRequestDTO;
import com.bok.integration.krypto.dto.WalletResponseDTO;
import com.bok.krypto.service.interfaces.WalletService;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/wallet")
public class WalletController {

    @Autowired
    WalletService walletService;

    @PostMapping("/create")
    WalletResponseDTO create(@RequestParam("userId") Long userid, @RequestBody WalletRequestDTO requestDTO) {
        return walletService.create(userid, requestDTO);
    }

    @PostMapping("/{userId}/delete")
    WalletDeleteResponseDTO delete(@RequestParam("userId") Long userId, @RequestBody WalletDeleteRequestDTO requestDTO) {
        return walletService.delete(userId, requestDTO);
    }

    @GetMapping("/{userId}")
    WalletsDTO wallets(@RequestParam("userId") Long userId) {
        return walletService.wallets(userId);
    }
}
