package com.nutriflow.repository;

import com.nutriflow.entity.MealCell;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MealCellRepository extends JpaRepository<MealCell, Long> {
    Optional<MealCell> findByWeekMenuIdAndDayIndexAndMealIndex(Long weekMenuId, int dayIndex, int mealIndex);
    List<MealCell> findByWeekMenuId(Long weekMenuId);

    @Query("SELECT mc FROM MealCell mc JOIN mc.weekMenu wm WHERE wm.user.id = :userId AND mc.dayIndex = :dayIndex AND mc.mealIndex = :mealIndex AND wm.weekStart = :weekStart")
    Optional<MealCell> findByUserAndWeekStartAndDayAndMeal(@Param("userId") Long userId,
                                                            @Param("weekStart") java.time.LocalDate weekStart,
                                                            @Param("dayIndex") int dayIndex,
                                                            @Param("mealIndex") int mealIndex);
}
