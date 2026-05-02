package com.nutriflow.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "meal_log_items")
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class MealLogItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meal_log_id", nullable = false)
    private MealLog mealLog;

    @Column(nullable = false, length = 200)
    private String foodName;

    @Column(precision = 7, scale = 2)
    private BigDecimal kcal;

    @Column(precision = 7, scale = 2)
    private BigDecimal kcalRangeMin;

    @Column(precision = 7, scale = 2)
    private BigDecimal kcalRangeMax;

    @Column(precision = 6, scale = 2)
    private BigDecimal protein;

    @Column(precision = 6, scale = 2)
    private BigDecimal carbs;

    @Column(precision = 6, scale = 2)
    private BigDecimal fat;

    @Column(precision = 7, scale = 2)
    private BigDecimal sodium;

    @Column(nullable = false, length = 20)
    @Builder.Default
    private String dataSource = "MANUAL";

    @Column(precision = 4, scale = 2)
    private BigDecimal confidence;

    @Column(nullable = false)
    @Builder.Default
    private Boolean photoUsed = false;

    @Column(length = 10)
    private String referenceType;

    @Column(nullable = false)
    @Builder.Default
    private Integer sortOrder = 0;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
