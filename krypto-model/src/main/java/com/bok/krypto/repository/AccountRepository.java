package com.bok.krypto.repository;

import com.bok.krypto.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByIdAndDeletedIsFalse(Long accountId);

    boolean existsByIdAndDeletedIsFalse(Long accountId);
}
