package com.bok.krypto.controller;

import com.bok.krypto.integration.internal.dto.ValidationRequestDTO;
import com.bok.krypto.integration.internal.dto.WalletDeleteRequestDTO;
import com.bok.krypto.integration.internal.dto.WalletDeleteResponseDTO;
import com.bok.krypto.integration.internal.dto.WalletInfoDTO;
import com.bok.krypto.integration.internal.dto.WalletRequestDTO;
import com.bok.krypto.integration.internal.dto.WalletResponseDTO;
import com.bok.krypto.integration.internal.dto.WalletsDTO;
import com.bok.krypto.service.interfaces.WalletService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequestMapping("/wallet")
public class WalletController {

    @Autowired
    WalletService walletService;

    @PostMapping("/create")
    @ApiOperation("Handles the wallet creation")
    public WalletResponseDTO create(@RequestParam("accountId") Long accountId, @RequestBody WalletRequestDTO requestDTO) {
        return walletService.create(accountId, requestDTO);
    }

    @PostMapping("/delete")
    public WalletDeleteResponseDTO delete(@RequestParam("accountId") Long accountId, @RequestBody WalletDeleteRequestDTO requestDTO) {
        return walletService.delete(accountId, requestDTO);
    }

    @GetMapping("/list")
    public WalletsDTO wallets(@RequestParam("accountId") Long accountId) {
        return walletService.listWallets(accountId);
    }

    @GetMapping("/{symbol}/info")
    public WalletInfoDTO walletInfo(@RequestParam("accountId") Long accountId, @PathVariable("symbol") String symbol, @RequestParam("startDate") Instant from, @RequestParam("endDate") Instant until) {
        return walletService.info(accountId, symbol, from, until);
    }

    @PostMapping("/validateAddress")
    public Boolean exists(@RequestParam("accountId") Long accountId, @RequestBody ValidationRequestDTO validationRequestDTO) {
        return walletService.validateAddress(validationRequestDTO);
    }
}
