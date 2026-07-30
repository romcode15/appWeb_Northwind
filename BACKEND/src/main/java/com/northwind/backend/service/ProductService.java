package com.northwind.backend.service;

import com.northwind.backend.dto.ProductRequest;
import com.northwind.backend.dto.ProductResponse;
import com.northwind.backend.entity.Category;
import com.northwind.backend.entity.Product;
import com.northwind.backend.entity.Supplier;
import com.northwind.backend.repository.CategoryRepository;
import com.northwind.backend.repository.ProductRepository;
import com.northwind.backend.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SupplierRepository supplierRepository;

    public List<ProductResponse> findAll() {
        return productRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public ProductResponse findById(Integer id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + id));
        return toResponse(product);
    }

    public ProductResponse create(ProductRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con id: " + request.getCategoryId()));

        Supplier supplier = supplierRepository.findById(request.getSupplierId())
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado con id: " + request.getSupplierId()));

        Product product = Product.builder()
                .productName(request.getProductName())
                .category(category)
                .supplier(supplier)
                .unitPrice(request.getUnitPrice())
                .unitsInStock(request.getUnitsInStock())
                .discontinued(request.getDiscontinued())
                .build();

        return toResponse(productRepository.save(product));
    }

    public ProductResponse update(Integer id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + id));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con id: " + request.getCategoryId()));

        Supplier supplier = supplierRepository.findById(request.getSupplierId())
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado con id: " + request.getSupplierId()));

        product.setProductName(request.getProductName());
        product.setCategory(category);
        product.setSupplier(supplier);
        product.setUnitPrice(request.getUnitPrice());
        product.setUnitsInStock(request.getUnitsInStock());
        product.setDiscontinued(request.getDiscontinued());

        return toResponse(productRepository.save(product));
    }

    public void delete(Integer id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + id));
        // Deshabilitar en lugar de borrar físico para conservar integridad referencial
        product.setDiscontinued(true);
        productRepository.save(product);
    }

    private ProductResponse toResponse(Product p) {
        return new ProductResponse(
                p.getProductId(),
                p.getProductName(),
                p.getCategory() != null ? p.getCategory().getCategoryId() : null,
                p.getCategory() != null ? p.getCategory().getCategoryName() : null,
                p.getSupplier() != null ? p.getSupplier().getSupplierId() : null,
                p.getSupplier() != null ? p.getSupplier().getCompanyName() : null,
                p.getUnitPrice(),
                p.getUnitsInStock(),
                p.getDiscontinued()
        );
    }
}
