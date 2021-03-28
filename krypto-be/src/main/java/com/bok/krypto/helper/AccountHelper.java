package com.bok.krypto.helper;

import com.bok.krypto.model.Account;
import org.springframework.stereotype.Component;

@Component
public class AccountHelper {

    public boolean existsById(Long userId) {
        return false;
    }

    public Account findById(Long userId) {
        return null;
    }

    public String findEmailByUserId(Long userId) {
        return null;
    }

    public void save(Long id) {

    }
}
