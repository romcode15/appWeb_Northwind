package com.northwind.backend.repository;

import com.northwind.backend.entity.OrderDetail;
import com.northwind.backend.entity.OrderDetailId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, OrderDetailId> {
}
