package com.nutriflow.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "week_menus")
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class WeekMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDate weekStart;

    @Column(columnDefinition = "TEXT")
    private String aiComment;

    @Column(nullable = false, length = 10)
    @Builder.Default
    private String generatedBy = "MANUAL";

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "weekMenu", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<MealCell> cells = new ArrayList<>();
}
