package com.nutriflow.service;

import com.nutriflow.dto.response.DailyNutritionResponse;
import com.nutriflow.dto.response.WeeklyNutritionResponse;
import com.nutriflow.entity.MealLog;
import com.nutriflow.entity.User;
import com.nutriflow.exception.ResourceNotFoundException;
import com.nutriflow.repository.MealLogRepository;
import com.nutriflow.repository.UserRepository;
import com.nutriflow.util.NutritionAdviceEngine;
import com.nutriflow.util.TDEECalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NutritionService {

    private final MealLogRepository mealLogRepository;
    private final UserRepository userRepository;
    private final TDEECalculator tdeeCalculator;
    private final NutritionAdviceEngine adviceEngine;

    @Transactional(readOnly = true)
    @Cacheable(value = "dailyNutrition", key = "#userId + ':' + #date")
    public DailyNutritionResponse getDailyNutrition(Long userId, LocalDate date) {
        User user = findUser(userId);
        List<MealLog> logs = mealLogRepository.findByUserIdAndMealDateOrderByMealIndex(userId, date);
        double targetKcal = tdeeCalculator.targetCalories(user);
        return computeDaily(date, logs, targetKcal);
    }

    @Transactional(readOnly = true)
    public WeeklyNutritionResponse getWeeklyNutrition(Long userId, LocalDate weekStart) {
        User user = findUser(userId);
        double targetKcal = tdeeCalculator.targetCalories(user);
        LocalDate weekEnd = weekStart.plusDays(6);

        List<DailyNutritionResponse> days = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            LocalDate date = weekStart.plusDays(i);
            List<MealLog> logs = mealLogRepository.findByUserIdAndMealDateOrderByMealIndex(userId, date);
            days.add(computeDaily(date, logs, targetKcal));
        }
        double avgKcal = days.stream().mapToDouble(d -> d.getTotalKcal() != null ? d.getTotalKcal() : 0).average().orElse(0);
        double avgProtein = days.stream().mapToDouble(d -> d.getTotalProtein() != null ? d.getTotalProtein() : 0).average().orElse(0);
        double avgCarbs = days.stream().mapToDouble(d -> d.getTotalCarbs() != null ? d.getTotalCarbs() : 0).average().orElse(0);
        double avgFat = days.stream().mapToDouble(d -> d.getTotalFat() != null ? d.getTotalFat() : 0).average().orElse(0);

        return WeeklyNutritionResponse.builder()
                .weekStart(weekStart).weekEnd(weekEnd).days(days)
                .avgKcal(round(avgKcal)).avgProtein(round(avgProtein))
                .avgCarbs(round(avgCarbs)).avgFat(round(avgFat)).targetKcal(targetKcal).build();
    }

    private DailyNutritionResponse computeDaily(LocalDate date, List<MealLog> logs, double targetKcal) {
        double kcal = 0, protein = 0, carbs = 0, fat = 0, sodium = 0;
        for (MealLog log : logs) {
            for (var item : log.getItems()) {
                kcal += dv(item.getKcal());
                protein += dv(item.getProtein());
                carbs += dv(item.getCarbs());
                fat += dv(item.getFat());
                sodium += dv(item.getSodium());
            }
        }
        double progress = targetKcal > 0 ? round(kcal / targetKcal * 100) : 0;
        String advice = adviceEngine.generateDailyAdvice(kcal, targetKcal, protein, carbs, fat);
        return DailyNutritionResponse.builder()
                .date(date).totalKcal(round(kcal)).totalProtein(round(protein))
                .totalCarbs(round(carbs)).totalFat(round(fat)).totalSodium(round(sodium))
                .targetKcal(round(targetKcal)).kcalProgress(progress).advice(advice).build();
    }

    private double dv(java.math.BigDecimal bd) {
        return bd != null ? bd.doubleValue() : 0;
    }

    private double round(double v) {
        return Math.round(v * 10.0) / 10.0;
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
