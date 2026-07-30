package com.northwind.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "suppliers")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Supplier {

    @Id
    @Column(name = "supplier_id")
    private Integer supplierId;

    @Column(name = "company_name")
    private String companyName;
}
