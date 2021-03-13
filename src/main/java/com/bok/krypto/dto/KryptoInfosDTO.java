package com.bok.krypto.dto;

import java.util.List;

public class KryptoInfosDTO {
    List<KryptoInfoDTO> kryptos;

    public KryptoInfosDTO(List<KryptoInfoDTO> info) {
        this.kryptos = info;
    }
}
