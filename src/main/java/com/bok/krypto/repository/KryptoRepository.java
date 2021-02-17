package com.bok.krypto.repository;


import com.bok.krypto.model.Krypto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KryptoRepository extends JpaRepository<Krypto, Long> {
}
