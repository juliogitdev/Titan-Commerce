package com.titan.commerce.modules.cart.service;

import com.titan.commerce.modules.cart.domain.Cart;
import com.titan.commerce.modules.cart.domain.enums.CartStatus;
import com.titan.commerce.modules.cart.repository.CartRepository;
import com.titan.commerce.modules.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;

    @Transactional
    public Cart getActiveCart(User user, String cartIdFromCookie) {

        if (user != null) {
            return cartRepository.findByUserAndStatus(user, CartStatus.OPEN)
                    .orElseGet(() -> createCart(user)); // Se não tiver, cria um novo para ele
        }

        if (cartIdFromCookie != null && !cartIdFromCookie.isBlank()) {
            return cartRepository.findByIdAndStatus(cartIdFromCookie, CartStatus.OPEN)
                    .orElseGet(() -> createCart(null)); // Se o ID do cookie for inválido/antigo, cria novo
        }

        return createCart(null);
    }


    private Cart createCart(User user) {
        Cart newCart = Cart.builder()
                .user(user)
                .status(CartStatus.OPEN)
                .active(true)
                .updatedAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusHours(24)) // Regra de expiração: Agora + 24h
                .build();

        return cartRepository.save(newCart);
    }
}