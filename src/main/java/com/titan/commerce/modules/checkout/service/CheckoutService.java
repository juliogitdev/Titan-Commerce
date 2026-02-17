package com.titan.commerce.modules.checkout.service;


import com.titan.commerce.modules.cart.domain.Cart;
import com.titan.commerce.modules.cart.domain.CartItem;
import com.titan.commerce.modules.cart.domain.enums.CartStatus;
import com.titan.commerce.modules.cart.repository.CartRepository;
import com.titan.commerce.modules.cart.service.CartService;
import com.titan.commerce.modules.catalog.domain.ProductVariant;
import com.titan.commerce.modules.catalog.repository.ProductVariantRepository;
import com.titan.commerce.modules.checkout.domain.Order;
import com.titan.commerce.modules.checkout.domain.OrderItem;
import com.titan.commerce.modules.checkout.domain.Payment;
import com.titan.commerce.modules.checkout.dto.CheckoutItemDTO;
import com.titan.commerce.modules.checkout.dto.CheckoutRequestDTO;
import com.titan.commerce.modules.checkout.enums.PaymentStatus;
import com.titan.commerce.modules.checkout.enums.StatusOrder;
import com.titan.commerce.modules.checkout.repository.OrderRepository;
import com.titan.commerce.modules.user.domain.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class CheckoutService {

    private final OrderRepository orderRepository;
    private final ProductVariantRepository productVariantRepository;
    private final CartRepository cartRepository;
    private final CartService cartService;

    @Transactional
    public Order processCheckout(User user, CheckoutRequestDTO request){
        //valida endereço
        //pendente

        //Busca o carrinho
        Cart cart = cartRepository.findByUserAndStatus(user, CartStatus.OPEN)
                .orElseThrow(() -> new RuntimeException("Carrinho não encontrado"));

        //Verifica se o carrinho não está vazio
        if(cart.getItems().isEmpty()){
            throw new RuntimeException("Carrinho vazio");
        }

        //prepara o pedido
        Order order = Order.builder()
                .user(user)
                .shippingAddressesId(request.addressId())
                .status(StatusOrder.PENDING_PAYMENT)
                .expiresAt(LocalDateTime.now().plusMinutes(30))
                .items(new ArrayList<>())
                .build();

        BigDecimal totalAmount = BigDecimal.ZERO;

        // processa os itens
        for(CartItem cartItem : cart.getItems()){
            ProductVariant productVariant = productVariantRepository.findByIdWithLock(cartItem.getProductVariant().getId())
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

            //Verifica se tem estoque
            if(productVariant.getStockQuantity() < cartItem.getQuantity()){
                throw new RuntimeException("Estoque insuficiente para o produto: " + productVariant.getProduct().getTitle());
            }

            //da baixa no estoque
            productVariant.setStockQuantity(productVariant.getStockQuantity() - cartItem.getQuantity());
            productVariantRepository.save(productVariant);

            //cria o item do pedido
            BigDecimal subtotal = productVariant.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .productVariant(productVariant)
                    .quantity(cartItem.getQuantity())
                    .unitPrice(productVariant.getPrice())
                    .subTotal(subtotal)
                    .build();

            order.getItems().add(orderItem);
            totalAmount = totalAmount.add(subtotal);

        }

        //Adiciona valor total na compra
        order.setTotalAmount(totalAmount);

        //cria pagamento
        Payment payment = Payment.builder()
                .order(order)
                .paymentMethod(request.paymentMethod())
                .status(PaymentStatus.PENDING)
                .build();

        //adiciona o pagamento na compra
        order.getPayments().add(payment);

        //Fecha carrinho
        cartService.setClosed(cart.getId());

        //salva a compra e retorna
        return orderRepository.save(order);
    }

}
