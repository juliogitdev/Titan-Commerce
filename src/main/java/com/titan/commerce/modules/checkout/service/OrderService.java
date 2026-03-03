package com.titan.commerce.modules.checkout.service;

import com.titan.commerce.modules.checkout.domain.Order;
import com.titan.commerce.modules.checkout.dto.OrderDetailsResponseDTO;
import com.titan.commerce.modules.checkout.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;


    public List<OrderDetailsResponseDTO> findOrdersByUser(Long userId) {
        List<Order> userOrders = orderRepository.findAllByUserIdOrderByCreatedAtDesc(userId);

        return userOrders.stream()
                .map(OrderDetailsResponseDTO::new)
                .collect(Collectors.toList());
    }


    public OrderDetailsResponseDTO findOrderByIdAndUser(String orderId, Long userId) {

        Order order = orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Pedido não encontrado ou você não tem permissão para acessá-lo."
                ));

        return new OrderDetailsResponseDTO(order);
    }


    public List<OrderDetailsResponseDTO> findAllOrders() {
        List<Order> allOrders = orderRepository.findAll();

        return allOrders.stream()
                .map(OrderDetailsResponseDTO::new)
                .collect(Collectors.toList());
    }
}