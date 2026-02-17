package com.titan.commerce.modules.checkout.service;

import com.titan.commerce.modules.catalog.domain.ProductVariant;
import com.titan.commerce.modules.catalog.repository.ProductVariantRepository;
import com.titan.commerce.modules.checkout.domain.Order;
import com.titan.commerce.modules.checkout.domain.OrderItem;
import com.titan.commerce.modules.checkout.enums.StatusOrder;
import com.titan.commerce.modules.checkout.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j // Para logs (System.out profissional)
public class OrderCleanupService {

    private final OrderRepository orderRepository;
    private final ProductVariantRepository productVariantRepository;

    //Roda a cada 1 minuto
    @Scheduled(fixedRate = 60000)
    @Transactional
    public void cancelUnpaidOrders() {
        log.info("🤖 Robô de limpeza iniciado...");

        // 1. Define o tempo limite (ex: pedidos feitos antes de 30 min atrás)
        LocalDateTime expirationTime = LocalDateTime.now().minusMinutes(30);

        // 2. Busca pedidos Pendentes e Velhos
        List<Order> expiredOrders = orderRepository.findOrdersByStatusAndCreatedAtBefore(
                StatusOrder.PENDING_PAYMENT,
                expirationTime
        );

        if (expiredOrders.isEmpty()) {
            log.info("✅ Nenhum pedido expirado encontrado.");
            return;
        }

        log.info("⚠️ Encontrados {} pedidos expirados. Iniciando cancelamento...", expiredOrders.size());

        for (Order order : expiredOrders) {
            // 3. Devolve itens para o estoque
            for (OrderItem item : order.getItems()) {
                ProductVariant variant = item.getProductVariant();

                int novaQuantidade = variant.getStockQuantity() + item.getQuantity();
                variant.setStockQuantity(novaQuantidade);

                productVariantRepository.save(variant);
                log.info("Estoque devolvido: Produto {} (+{})", variant.getProduct().getTitle(), item.getQuantity());
            }

            // 4. Atualiza status do pedido
            order.setStatus(StatusOrder.CANCELLED);
            orderRepository.save(order);

            log.info("❌ Pedido {} cancelado automaticamente.", order.getId());
        }
    }
}