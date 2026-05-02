package com.nutriflow.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "meal_cells")
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class MealCell {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "week_menu_id", nullable = false)
    private WeekMenu weekMenu;

    @Column(nullable = false)
    private Integer dayIndex;

    @Column(nullable = false)
    private Integer mealIndex;

    @Column(nullable = false, length = 15)
    @Builder.Default
    private String mealType = "外食";

    @Column(nullable = false)
    @Builder.Default
    private Boolean logged = false;

    private LocalTime logTime;

    @Column(length = 255)
    private String note;

    @Column(nullable = false, length = 10)
    @Builder.Default
    private String source = "MANUAL";

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "mealCell", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("sortOrder ASC")
    @Builder.Default
    private List<MealItem> items = new ArrayList<>();
}
