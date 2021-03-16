package com.bok.krypto.repository;

import com.bok.krypto.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
    boolean existsById(UUID id);

    Optional<Wallet> findById(UUID walletId);

    Optional<Wallet> findByUser_IdAndKrypto_Symbol(Long userId, String symbol);

    Boolean existsByUser_IdAndKrypto_SymbolAndAvailableAmountGreaterThanEqual(Long userId, String symbol, BigDecimal amount);

    Boolean existsByUser_IdAndKrypto_Symbol(Long userId, String symbol);

    @Query("select w from Wallet w where w.status = 'PENDING' ")
    List<Wallet> findAllPendingWallets();
}
