package com.northwind.backend.service;

import com.northwind.backend.entity.Category;
import com.northwind.backend.entity.Customer;
import com.northwind.backend.entity.Supplier;
import com.northwind.backend.repository.CategoryRepository;
import com.northwind.backend.repository.CustomerRepository;
import com.northwind.backend.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CatalogService {

    private final CategoryRepository categoryRepository;
    private final SupplierRepository supplierRepository;
    private final CustomerRepository customerRepository;

    public List<Category> findAllCategories() {
        return categoryRepository.findAll();
    }

    public List<Supplier> findAllSuppliers() {
        return supplierRepository.findAll();
    }

    public List<Customer> findAllCustomers() {
        return customerRepository.findAll();
    }
}
