package com.northwind.backend.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductRequest {

    @NotBlank(message = "El nombre del producto no puede estar vacío")
    private String productName;

    @NotNull(message = "La categoría es obligatoria")
    private Integer categoryId;

    @NotNull(message = "El proveedor es obligatorio")
    private Integer supplierId;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor que cero")
    private BigDecimal unitPrice;

    @NotNull(message = "Las unidades son obligatorias")
    @Min(value = 0, message = "Las unidades no pueden ser negativas")
    private Integer unitsInStock;

    private Integer discontinued = 0; // 0 = activo, 1 = descontinuado
}
