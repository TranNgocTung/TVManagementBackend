package com.example.tvmanagerment.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int productId;
    private String productName;
    private BigDecimal price;

    private String imageUrl;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at_product")
    private LocalDateTime createdAtProduct;

    @PrePersist
    public void prePersist() {
        createdAtProduct = LocalDateTime.now();
    }
    @ManyToOne
    private ProductCategory category;

}
