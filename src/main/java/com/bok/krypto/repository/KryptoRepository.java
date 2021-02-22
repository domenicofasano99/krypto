package com.bok.krypto.repository;


import com.bok.krypto.model.Krypto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface KryptoRepository extends JpaRepository<Krypto, Long> {

    Optional<Krypto> findBySymbol(String name);
    List<Krypto> findBySymbolIn(List<String> codes);
}
