package com.titan.commerce.modules.checkout.repository;

import com.titan.commerce.modules.checkout.domain.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, String> {

}
