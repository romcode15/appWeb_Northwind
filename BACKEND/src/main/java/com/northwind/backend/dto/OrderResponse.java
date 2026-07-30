package com.northwind.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class OrderResponse {
    private Integer orderId;
    private String customerId;
    private String customerName;
    private Integer appUserId;
    private String appUserFullName;
    private LocalDate orderDate;
    private List<OrderDetailResponse> details;
    private BigDecimal total;
}
