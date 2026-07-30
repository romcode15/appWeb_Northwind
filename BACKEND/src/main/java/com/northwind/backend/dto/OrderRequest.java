package com.northwind.backend.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Data
public class OrderRequest {

    @NotBlank(message = "El cliente es obligatorio")
    private String customerId;

    @NotNull(message = "El usuario es obligatorio")
    private Integer appUserId;

    @NotEmpty(message = "El carrito no puede estar vacío")
    @Valid
    private List<OrderItemRequest> items;
}
