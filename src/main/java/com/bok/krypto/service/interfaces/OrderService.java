package com.bok.krypto.service.interfaces;

import com.bok.krypto.dto.OrderRequestDTO;
import com.bok.krypto.dto.OrderResponseDTO;

public interface OrderService {

    OrderResponseDTO placeOrder(OrderRequestDTO orderRequestDTO);
}
