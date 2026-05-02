package com.nutriflow.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "food_preferences")
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class FoodPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 100)
    private String foodName;

    @Column(nullable = false, length = 20)
    private String category;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isCustom = false;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isAllergy = false;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
