package com.titan.commerce.modules.cart.controller;

import com.titan.commerce.modules.cart.domain.Cart;
import com.titan.commerce.modules.cart.dto.CartItemRequestDTO;
import com.titan.commerce.modules.cart.dto.CartResponseDTO;
import com.titan.commerce.modules.cart.service.CartService;
import com.titan.commerce.modules.user.domain.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    //Busca o carrinho atual
    @GetMapping
    public ResponseEntity<CartResponseDTO> getCart(
            @AuthenticationPrincipal User user,
            @CookieValue(name = "cart_id", required = false) String cartIdCookie // Pega o Cookie (se existir)
    ) {

        // --- ADICIONE ISSO PARA DEBUGAR ---
        System.out.println("DEBUG DO CONTROLLER:");
        System.out.println("User está nulo? " + (user == null));
        System.out.println("Cookie chegou? " + cartIdCookie);
        // ----------------------------------

        Cart cart = cartService.getActiveCart(user, cartIdCookie);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, buildCartCookie(cart.getId()).toString()) // Renova o cookie
                .body(CartResponseDTO.fromEntity(cart));
    }

    //Adiciona item
    @PostMapping("/items")
    public ResponseEntity<CartResponseDTO> addItem(
            @AuthenticationPrincipal User user,
            @CookieValue(name = "cart_id", required = false) String cartIdCookie,
            @RequestBody @Valid CartItemRequestDTO dto
    ) {
        Cart cart = cartService.addItemToCart(user, cartIdCookie, dto);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, buildCartCookie(cart.getId()).toString()) // Garante que o cookie seja setado
                .body(CartResponseDTO.fromEntity(cart));
    }

    //Remove item
    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<Void> removeItem(
            @AuthenticationPrincipal User user,
            @CookieValue(name = "cart_id", required = false) String cartIdCookie,
            @PathVariable Long itemId
    ) {
        cartService.removeItem(user, cartIdCookie, itemId);
        return ResponseEntity.noContent().build();
    }

    private ResponseCookie buildCartCookie(String cartId) {
        return ResponseCookie.from("cart_id", cartId)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(Duration.ofDays(7)) // Expira em 7 dias
                .sameSite("Lax")
                .build();
    }
}