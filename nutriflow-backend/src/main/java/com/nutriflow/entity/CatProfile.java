package com.nutriflow.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "cat_profiles")
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class CatProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false, length = 20)
    private String breed;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(precision = 6, scale = 4)
    private BigDecimal knucklePxRatio;

    @Column(precision = 6, scale = 2)
    private BigDecimal bowlWidthMm;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
