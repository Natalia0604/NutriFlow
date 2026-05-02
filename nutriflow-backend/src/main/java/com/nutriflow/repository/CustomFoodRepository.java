package com.nutriflow.repository;

import com.nutriflow.entity.CustomFood;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomFoodRepository extends JpaRepository<CustomFood, Long> {
    List<CustomFood> findByUserId(Long userId);
    List<CustomFood> findByUserIdAndFoodNameContainingIgnoreCase(Long userId, String foodName);
    Optional<CustomFood> findByIdAndUserId(Long id, Long userId);
    void deleteByIdAndUserId(Long id, Long userId);
}
