package com.nutriflow.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "meal_logs")
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class MealLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meal_cell_id")
    private MealCell mealCell;

    @Column(nullable = false)
    private LocalDate mealDate;

    @Column(nullable = false)
    private Integer mealIndex;

    @Column(nullable = false, length = 15)
    @Builder.Default
    private String mealType = "EATING_OUT";

    @Column(length = 100)
    private String storeName;

    @Column(length = 255)
    private String note;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime loggedAt;

    @OneToMany(mappedBy = "mealLog", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("sortOrder ASC")
    @Builder.Default
    private List<MealLogItem> items = new ArrayList<>();
}
