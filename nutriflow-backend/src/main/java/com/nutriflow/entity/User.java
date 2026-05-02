package com.nutriflow.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    private String gender;

    private Integer age;

    @Column(precision = 5, scale = 2)
    private BigDecimal weight;

    @Column(precision = 5, scale = 2)
    private BigDecimal height;

    private String activityLevel;

    @Column(nullable = false)
    @Builder.Default
    private Boolean hasWeightGoal = false;

    @Column(precision = 5, scale = 2)
    private BigDecimal targetWeight;

    private Integer goalWeeks;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isSetupComplete = false;

    private String refreshToken;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private CatProfile catProfile;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<WeekMenu> weekMenus = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<MealLog> mealLogs = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<WeightRecord> weightRecords = new ArrayList<>();
}
