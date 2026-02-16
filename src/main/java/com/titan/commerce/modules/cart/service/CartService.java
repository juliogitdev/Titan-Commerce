package com.titan.commerce.modules.cart.service;

import com.titan.commerce.modules.cart.domain.Cart;
import com.titan.commerce.modules.cart.domain.CartItem;
import com.titan.commerce.modules.cart.domain.enums.CartStatus;
import com.titan.commerce.modules.cart.dto.CartItemRequestDTO;
import com.titan.commerce.modules.cart.repository.CartItemRepository;
import com.titan.commerce.modules.cart.repository.CartRepository;
import com.titan.commerce.modules.catalog.domain.ProductVariant;
import com.titan.commerce.modules.catalog.repository.ProductVariantRepository;
import com.titan.commerce.modules.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductVariantRepository productVariantRepository;

    @Transactional
    public Cart getActiveCart(User user, String cartIdFromCookie) {

        if (user != null && cartIdFromCookie != null && !cartIdFromCookie.isBlank()) {
            mergeCarts(user, cartIdFromCookie);
        }


        if (user != null) {
            return cartRepository.findByUserAndStatus(user, CartStatus.OPEN)
                    .orElseGet(() -> createCart(user));
        }

        if (cartIdFromCookie != null && !cartIdFromCookie.isBlank()) {
            return cartRepository.findByIdAndStatus(cartIdFromCookie, CartStatus.OPEN)
                    .orElseGet(() -> createCart(null));
        }

        return createCart(null);
    }


    private Cart createCart(User user) {
        Cart newCart = Cart.builder()
                .user(user)
                .status(CartStatus.OPEN)
                .active(true)
                .updatedAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusHours(48))
                .build();

        return cartRepository.save(newCart);
    }

    @Transactional
    public Cart addItemToCart(User user, String cartIdFromCookie, CartItemRequestDTO dto) {
        Cart cart = getActiveCart(user, cartIdFromCookie);

        // 1. Busca o Produto Real
        ProductVariant variant = productVariantRepository.findById(dto.productVariantId())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        // 2. Valida Estoque
        if (variant.getStockQuantity() < dto.quantity()) {
            throw new RuntimeException("Estoque insuficiente.");
        }

        // 3. Busca item existente usando o NOVO método do repositório
        Optional<CartItem> existingItem = cartItemRepository
                .findByCartIdAndProductVariantId(cart.getId(), dto.productVariantId());

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();

            // Valida estoque acumulado
            if (variant.getStockQuantity() < item.getQuantity() + dto.quantity()) {
                throw new RuntimeException("Estoque insuficiente para essa quantidade total.");
            }

            item.setQuantity(item.getQuantity() + dto.quantity());
            item.setPrice(variant.getPrice()); // Atualiza preço se mudou no catálogo
            cartItemRepository.save(item);
        } else {
            CartItem newItem = CartItem.builder()
                    .cart(cart)
                    .productVariant(variant) // <--- Passamos o OBJETO agora, não o ID
                    .quantity(dto.quantity())
                    .price(variant.getPrice()) // Pega preço do banco
                    .build();

            cart.getItems().add(newItem);
            cartItemRepository.save(newItem);
        }

        cart.setUpdatedAt(LocalDateTime.now());
        return cartRepository.save(cart);
    }

    @Transactional
    public void removeItem(User user, String cartIdFromCookie, Long itemId) {
        Cart cart = getActiveCart(user, cartIdFromCookie);

        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item não encontrado"));

        if (!item.getCart().getId().equals(cart.getId())) {
            throw new RuntimeException("Item não pertence a este carrinho");
        }

        cartItemRepository.delete(item);
    }

    @Transactional
    public void mergeCarts(User user, String cookieCartId) {
        // Tenta buscar o carrinho anônimo pelo ID do cookie
        Optional<Cart> anonymousCartOpt = cartRepository.findByIdAndStatus(cookieCartId, CartStatus.OPEN);


        if (anonymousCartOpt.isEmpty()) {
            return;
        }

        Cart anonymousCart = anonymousCartOpt.get();

        //Se este carrinho "anônimo" já tiver um dono (e não for o usuário atual),
        if (anonymousCart.getUser() != null && !anonymousCart.getUser().getId().equals(user.getId())) {
            return;
        }

        // Se o carrinho do cookie já for do próprio usuário, não faz nada.
        if (anonymousCart.getUser() != null && anonymousCart.getUser().getId().equals(user.getId())) {
            return;
        }

        // Busca o carrinho existente do usuário no banco
        Optional<Cart> userCartOpt = cartRepository.findByUserAndStatus(user, CartStatus.OPEN);

        if (userCartOpt.isPresent()) {
            Cart userCart = userCartOpt.get();

            for (CartItem anonItem : anonymousCart.getItems()) {
                // Verifica se o usuário já tem esse mesmo produto no carrinho dele
                Optional<CartItem> existingUserItem = userCart.getItems().stream()
                        .filter(item -> item.getProductVariant().getId().equals(anonItem.getProductVariant().getId()))
                        .findFirst();

                if (existingUserItem.isPresent()) {
                    // Se já tem, soma a quaatidade
                    CartItem userItem = existingUserItem.get();
                    userItem.setQuantity(userItem.getQuantity() + anonItem.getQuantity());
                    cartItemRepository.save(userItem);
                } else {
                    // Se não tem, joga o item para o carrinho do usuário
                    anonItem.setCart(userCart);
                    userCart.getItems().add(anonItem);
                    cartItemRepository.save(anonItem);
                }
            }

            // Atualiza o timestamp do carrinho do usuário
            userCart.setUpdatedAt(LocalDateTime.now());
            cartRepository.save(userCart);

            // Deleta o carrinho anônimo
            cartRepository.delete(anonymousCart);

        } else {

            anonymousCart.setUser(user);
            anonymousCart.setUpdatedAt(LocalDateTime.now());
            cartRepository.save(anonymousCart);
        }
    }
}