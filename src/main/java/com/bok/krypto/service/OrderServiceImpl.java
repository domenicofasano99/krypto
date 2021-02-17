package com.bok.krypto.service;


import com.bok.krypto.dto.OrderRequestDTO;
import com.bok.krypto.dto.OrderResponseDTO;
import com.bok.krypto.service.interfaces.OrderService;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {
    @Override
    public OrderResponseDTO placeOrder(OrderRequestDTO orderRequestDTO) {
        return null;
    }
}
