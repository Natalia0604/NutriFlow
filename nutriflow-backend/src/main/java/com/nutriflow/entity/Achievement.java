package com.nutriflow.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "achievements")
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class Achievement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 255)
    private String description;

    @Column(nullable = false, length = 10)
    private String icon;

    @Column(name = "condition_json", columnDefinition = "JSON")
    private String conditionJson;

    @Column(nullable = false)
    @Builder.Default
    private Integer sortOrder = 0;
}
