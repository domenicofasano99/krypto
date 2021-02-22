package com.bok.krypto.helper;

import com.bok.krypto.exception.KryptoNotFoundException;
import com.bok.krypto.model.Krypto;
import com.bok.krypto.repository.KryptoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class KryptoHelper {

    @Autowired
    KryptoRepository kryptoRepository;

    public Krypto findByCode(String code) {
        return kryptoRepository.findBySymbol(code).orElseThrow(() -> new KryptoNotFoundException("krypto not found"));
    }

}
