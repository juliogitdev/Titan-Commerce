package com.titan.commerce.modules.checkout.repository;

import com.titan.commerce.modules.checkout.domain.Order;
import com.titan.commerce.modules.checkout.enums.PaymentMethod;
import com.titan.commerce.modules.checkout.enums.PaymentStatus;
import com.titan.commerce.modules.checkout.enums.StatusOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {


    //Busca compras pelo usuario
    List<Order> findOrderByUserId(Long id);

    //Busca por status da compra (Ex: pendente pagamento, cancelada, enviada e etc)
    List<Order> findOrdersByStatusAndCreatedAtBefore(StatusOrder statusOrder, LocalDateTime dateTime);

    // Busca todos os pedidos de um usuário específico, ordenando do mais novo para o mais antigo
    List<Order> findAllByUserIdOrderByCreatedAtDesc(Long userId);

    // Busca um pedido específico garantindo que ele pertence àquele usuário
    Optional<Order> findByIdAndUserId(String orderId, Long userId);

    //Busca uma compra pelo id trazendo pagamento e itens
    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.items LEFT JOIN FETCH o.payments WHERE o.id = :id")
    Optional<Order> findByIdWithItems(@Param("id") String id);

}
