package com.northwind.backend.repository;

import com.northwind.backend.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    // Solo las órdenes creadas desde la aplicación (tienen app_user_id)
    @Query("SELECT o FROM Order o WHERE o.appUser IS NOT NULL ORDER BY o.orderId DESC")
    List<Order> findAppOrders();
}
