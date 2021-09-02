package com.bok.krypto.repository;

import com.bok.krypto.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Long> {

    Optional<Wallet> findByAddressAndDeletedIsFalse(String address);

    Optional<Wallet> findByAccount_IdAndKrypto_SymbolAndDeletedIsFalse(Long accountId, String symbol);

    Boolean existsByAccount_IdAndKrypto_SymbolAndDeletedIsFalse(Long accountId, String symbol);

    @Query("select count(w.id) from Wallet w where w.status like 'PENDING' ")
    Integer countPendingWallets();

    List<Wallet> findByAccount_IdAndDeletedIsFalse(Long accountId);

    Boolean existsByAddressAndKrypto_SymbolAndDeletedIsFalse(String address, String krypto_symbol);

    Boolean existsByAddress(String address);
}
