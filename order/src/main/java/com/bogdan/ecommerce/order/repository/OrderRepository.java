package com.bogdan.ecommerce.order.repository;

import com.bogdan.ecommerce.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {

}
