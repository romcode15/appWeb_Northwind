package com.northwind.backend.controller;

import com.northwind.backend.service.CatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CatalogController {

    private final CatalogService catalogService;

    @GetMapping("/categories")
    public ResponseEntity<?> getCategories() {
        return ResponseEntity.ok(catalogService.findAllCategories());
    }

    @GetMapping("/suppliers")
    public ResponseEntity<?> getSuppliers() {
        return ResponseEntity.ok(catalogService.findAllSuppliers());
    }

    @GetMapping("/customers")
    public ResponseEntity<?> getCustomers() {
        return ResponseEntity.ok(catalogService.findAllCustomers());
    }
}
