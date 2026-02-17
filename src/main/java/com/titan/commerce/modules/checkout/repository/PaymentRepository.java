package com.titan.commerce.modules.checkout.repository;

import com.titan.commerce.modules.checkout.domain.Payment;
import com.titan.commerce.modules.checkout.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {

    //Busca pagamento pelo id da compra
    List<Payment> findPaymentByOrderId(String orderId);

    //Verifica se já recebeu webhook de pagamento
    Boolean existsByTransactionId(String transactionId);


    Optional<Payment> findByOrderIdAndStatus(String orderId, PaymentStatus status);
}
