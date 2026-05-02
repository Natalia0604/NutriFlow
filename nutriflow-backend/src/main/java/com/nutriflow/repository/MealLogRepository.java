package com.nutriflow.repository;

import com.nutriflow.entity.MealLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MealLogRepository extends JpaRepository<MealLog, Long> {
    List<MealLog> findByUserIdAndMealDateOrderByMealIndex(Long userId, LocalDate mealDate);
    Page<MealLog> findByUserIdOrderByLoggedAtDesc(Long userId, Pageable pageable);
    Optional<MealLog> findByIdAndUserId(Long id, Long userId);
}
