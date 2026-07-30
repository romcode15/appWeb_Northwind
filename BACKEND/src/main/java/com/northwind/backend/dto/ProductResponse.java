package com.northwind.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ProductResponse {
    private Integer productId;
    private String productName;
    private Integer categoryId;
    private String categoryName;
    private Integer supplierId;
    private String supplierName;
    private BigDecimal unitPrice;
    private Integer unitsInStock;
    private Boolean discontinued;
}
