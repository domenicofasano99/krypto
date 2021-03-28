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

    Optional<Wallet> findByAccount_IdAndKrypto_Symbol(Long accountId, String symbol);

    Boolean existsByAccount_IdAndKrypto_SymbolAndAvailableAmountGreaterThanEqual(Long accountId, String symbol, BigDecimal amount);

    Boolean existsByAccount_IdAndKrypto_Symbol(Long accountId, String symbol);

    @Query("select count(w.id) from Wallet w where w.status = 'PENDING' ")
    Integer countPendingWallets();

    List<Wallet> findByAccount_Id(Long accountId);
}
