package com.northwind.backend.service;

import com.northwind.backend.dto.*;
import com.northwind.backend.entity.*;
import com.northwind.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final CustomerRepository customerRepository;
    private final AppUserRepository appUserRepository;
    private final ProductRepository productRepository;

    @Transactional
    public OrderResponse createOrder(OrderRequest request) {
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado: " + request.getCustomerId()));

        AppUser appUser = appUserRepository.findById(request.getAppUserId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + request.getAppUserId()));

        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new RuntimeException("El carrito está vacío");
        }

        Order order = Order.builder()
                .customer(customer)
                .appUser(appUser)
                .orderDate(LocalDate.now())
                .status("Pendiente")
                .build();

        Order savedOrder = orderRepository.save(order);

        List<OrderDetail> details = new ArrayList<>();
        for (OrderItemRequest item : request.getItems()) {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + item.getProductId()));

            if (Integer.valueOf(1).equals(product.getDiscontinued())) {
                throw new RuntimeException("El producto '" + product.getProductName() + "' está descontinuado");
            }
            if (item.getQuantity() <= 0) {
                throw new RuntimeException("La cantidad debe ser mayor que cero");
            }
            if (item.getQuantity() > product.getUnitsInStock()) {
                throw new RuntimeException("Stock insuficiente para '" + product.getProductName() +
                        "'. Disponible: " + product.getUnitsInStock());
            }

            OrderDetail detail = OrderDetail.builder()
                    .id(new OrderDetailId(savedOrder.getOrderId(), product.getProductId()))
                    .order(savedOrder)
                    .product(product)
                    .unitPrice(product.getUnitPrice())
                    .quantity(item.getQuantity())
                    .discount(item.getDiscount() != null ? item.getDiscount() : 0.0)
                    .build();

            details.add(orderDetailRepository.save(detail));

            product.setUnitsInStock(product.getUnitsInStock() - item.getQuantity());
            productRepository.save(product);
        }

        return buildResponse(savedOrder, customer, appUser, details);
    }

    // Solo órdenes creadas desde la aplicación
    public List<OrderResponse> findAll() {
        return orderRepository.findAppOrders()
                .stream()
                .map(o -> buildResponse(o, o.getCustomer(), o.getAppUser(),
                        o.getOrderDetails() != null ? o.getOrderDetails() : Collections.emptyList()))
                .collect(Collectors.toList());
    }

    public OrderResponse findById(Integer id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada: " + id));
        return buildResponse(order, order.getCustomer(), order.getAppUser(),
                order.getOrderDetails() != null ? order.getOrderDetails() : Collections.emptyList());
    }

    private OrderResponse buildResponse(Order order, Customer customer, AppUser appUser, List<OrderDetail> details) {
        List<OrderDetailResponse> detailResponses = details.stream()
                .filter(d -> d != null && d.getProduct() != null && d.getUnitPrice() != null)
                .map(d -> {
                    BigDecimal price   = d.getUnitPrice();
                    int qty            = d.getQuantity();
                    double disc        = d.getDiscount() != null ? d.getDiscount() : 0.0;
                    BigDecimal subtotal = price
                            .multiply(BigDecimal.valueOf(qty))
                            .multiply(BigDecimal.valueOf(1 - disc));

                    return new OrderDetailResponse(
                            d.getProduct().getProductId(),
                            d.getProduct().getProductName(),
                            price, qty, disc, subtotal
                    );
                }).collect(Collectors.toList());

        BigDecimal total = detailResponses.stream()
                .map(OrderDetailResponse::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new OrderResponse(
                order.getOrderId(),
                customer != null ? customer.getCustomerId() : "",
                customer != null ? customer.getCompanyName() : "",
                appUser  != null ? appUser.getUserId()  : null,
                appUser  != null ? appUser.getFullName() : "",
                order.getOrderDate(),
                detailResponses,
                total
        );
    }
}
