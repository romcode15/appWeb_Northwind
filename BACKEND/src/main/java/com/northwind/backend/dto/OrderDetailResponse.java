package com.northwind.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class OrderDetailResponse {
    private Integer productId;
    private String productName;
    private BigDecimal unitPrice;
    private Integer quantity;
    private Double discount;
    private BigDecimal subtotal;
}
