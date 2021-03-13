package com.bok.krypto.dto;

import java.util.List;

public class PricesResponseDTO {

    public List<PriceResponseDTO> prices;

    public PricesResponseDTO(List<PriceResponseDTO> prices) {
        this.prices = prices;
    }
}
