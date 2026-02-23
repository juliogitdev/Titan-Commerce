package com.titan.commerce.modules.cart.controller;

import com.titan.commerce.modules.cart.domain.Cart;
import com.titan.commerce.modules.cart.dto.CartItemRequestDTO;
import com.titan.commerce.modules.cart.dto.CartResponseDTO;
import com.titan.commerce.modules.cart.service.CartService;
import com.titan.commerce.modules.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Carrinho de Compras", description = "Gestão do carrinho. Funciona para usuários autenticados (via Token JWT) e anônimos (via Cookie).")
public class CartController {

    private final CartService cartService;

    @Operation(summary = "Buscar carrinho atual",
            description = "Retorna o carrinho do usuário logado ou o carrinho anônimo atrelado ao cookie `cart_id`. Renova o tempo de vida do cookie na resposta.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Carrinho recuperado com sucesso",
                    headers = @Header(name = "Set-Cookie", description = "Cookie com o ID do carrinho renovado por 7 dias", schema = @Schema(type = "string")))
    })
    @GetMapping
    public ResponseEntity<CartResponseDTO> getCart(
            @Parameter(hidden = true) @AuthenticationPrincipal User user,
            @Parameter(description = "ID do carrinho armazenado no navegador (simulação de sessão anônima)")
            @CookieValue(name = "cart_id", required = false) String cartIdCookie
    ) {
        Cart cart = cartService.getActiveCart(user, cartIdCookie);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, buildCartCookie(cart.getId()).toString())
                .body(CartResponseDTO.fromEntity(cart));
    }

    @Operation(summary = "Adicionar item ao carrinho",
            description = "Adiciona uma nova variante de produto ao carrinho ou atualiza a quantidade se já existir.")
    @ApiResponse(responseCode = "200", description = "Item adicionado. Retorna o estado atualizado do carrinho.")
    @PostMapping("/items")
    public ResponseEntity<CartResponseDTO> addItem(
            @Parameter(hidden = true) @AuthenticationPrincipal User user,
            @Parameter(description = "ID do carrinho armazenado no navegador")
            @CookieValue(name = "cart_id", required = false) String cartIdCookie,
            @RequestBody @Valid CartItemRequestDTO dto
    ) {
        Cart cart = cartService.addItemToCart(user, cartIdCookie, dto);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, buildCartCookie(cart.getId()).toString())
                .body(CartResponseDTO.fromEntity(cart));
    }

    @Operation(summary = "Remover item",
            description = "Remove completamente uma variante de produto do carrinho atual.")
    @ApiResponse(responseCode = "204", description = "Item removido com sucesso")
    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<Void> removeItem(
            @Parameter(hidden = true) @AuthenticationPrincipal User user,
            @Parameter(description = "ID do carrinho armazenado no navegador")
            @CookieValue(name = "cart_id", required = false) String cartIdCookie,
            @Parameter(description = "ID interno do item dentro do carrinho") @PathVariable Long itemId
    ) {
        cartService.removeItem(user, cartIdCookie, itemId);
        return ResponseEntity.noContent().build();
    }

    private ResponseCookie buildCartCookie(String cartId) {
        return ResponseCookie.from("cart_id", cartId)
                .httpOnly(true)
                .secure(false) // Mude para true em produção (HTTPS)
                .path("/")
                .maxAge(Duration.ofDays(7))
                .sameSite("Lax")
                .build();
    }
}