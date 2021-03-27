package com.bok.krypto.helper;

import com.bok.krypto.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserHelper {

    public boolean existsById(Long userId) {
        return false;
    }

    public User findById(Long userId) {
        return null;
    }

    public String findEmailByUserId(Long userId) {
        return null;
    }

    public void save(Long id) {

    }
}
