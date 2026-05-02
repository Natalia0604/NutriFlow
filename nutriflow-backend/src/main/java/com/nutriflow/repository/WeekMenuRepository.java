package com.nutriflow.repository;

import com.nutriflow.entity.WeekMenu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface WeekMenuRepository extends JpaRepository<WeekMenu, Long> {
    Optional<WeekMenu> findByUserIdAndWeekStart(Long userId, LocalDate weekStart);
    List<WeekMenu> findByUserIdOrderByWeekStartDesc(Long userId);
}
