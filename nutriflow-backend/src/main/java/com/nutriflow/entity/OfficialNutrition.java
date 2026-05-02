package com.nutriflow.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "official_nutrition")
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class OfficialNutrition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String storeName;

    @Column(nullable = false, length = 200)
    private String itemName;

    @Column(length = 50)
    private String sizeLabel;

    @Column(nullable = false, precision = 7, scale = 2)
    private BigDecimal kcal;

    @Column(precision = 6, scale = 2)
    private BigDecimal protein;

    @Column(precision = 6, scale = 2)
    private BigDecimal carbs;

    @Column(precision = 6, scale = 2)
    private BigDecimal fat;

    @Column(precision = 7, scale = 2)
    private BigDecimal sodium;

    @Column(precision = 6, scale = 2)
    private BigDecimal sugar;

    @Column(length = 500)
    private String dataUrl;

    private LocalDate lastVerified;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
