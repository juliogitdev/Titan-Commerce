package com.titan.commerce.modules.cart.repository;

import com.titan.commerce.modules.cart.domain.Cart;
import com.titan.commerce.modules.cart.domain.enums.CartStatus;
import com.titan.commerce.modules.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, String> { // ID é String (UUID)

    // Buscar carrinho aberto de usuário logado
    Optional<Cart> findByUserAndStatus(User user, CartStatus status);

    // Buscar carrinho anônimo pelo ID (cookie)
    Optional<Cart> findByIdAndStatus(String id, CartStatus status);

}