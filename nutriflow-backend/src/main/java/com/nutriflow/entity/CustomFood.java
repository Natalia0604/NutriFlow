package com.nutriflow.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "custom_foods")
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class CustomFood {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 200)
    private String foodName;

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

    @Column(name = "serving_g", precision = 6, scale = 2)
    private BigDecimal servingG;

    @Column(length = 255)
    private String note;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
