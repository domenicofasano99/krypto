package com.bok.krypto.repository;

import com.bok.krypto.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
    boolean existsById(UUID id);

    Optional<Wallet> findById(UUID walletId);

    Optional<Wallet> findByUser_IdAndKrypto_Symbol(Long userId, String symbol);
}
