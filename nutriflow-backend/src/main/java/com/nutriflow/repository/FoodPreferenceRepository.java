package com.nutriflow.repository;

import com.nutriflow.entity.FoodPreference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FoodPreferenceRepository extends JpaRepository<FoodPreference, Long> {
    List<FoodPreference> findByUserId(Long userId);
    Optional<FoodPreference> findByUserIdAndFoodName(Long userId, String foodName);
    void deleteByIdAndUserId(Long id, Long userId);
}
