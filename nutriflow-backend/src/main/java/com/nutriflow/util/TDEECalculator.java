package com.nutriflow.util;

import com.nutriflow.entity.User;
import org.springframework.stereotype.Component;

@Component
public class TDEECalculator {

    public double calculate(User user) {
        double bmr = computeBMR(user.getGender(), user.getWeight().doubleValue(),
                user.getHeight().doubleValue(), user.getAge());
        return bmr * activityFactor(user.getActivityLevel());
    }

    public double targetCalories(User user) {
        double tdee = calculate(user);
        if (!Boolean.TRUE.equals(user.getHasWeightGoal())
                || user.getTargetWeight() == null
                || user.getGoalWeeks() == null) {
            return tdee;
        }
        double weightDiff = user.getTargetWeight().doubleValue() - user.getWeight().doubleValue();
        double weeklyKcalDelta = (weightDiff * 7700) / user.getGoalWeeks();
        double dailyDelta = weeklyKcalDelta / 7;
        double target = tdee + dailyDelta;
        return Math.max(target, 1200);
    }

    private double computeBMR(String gender, double weightKg, double heightCm, int age) {
        if ("female".equalsIgnoreCase(gender)) {
            return 10 * weightKg + 6.25 * heightCm - 5 * age - 161;
        }
        return 10 * weightKg + 6.25 * heightCm - 5 * age + 5;
    }

    private double activityFactor(String level) {
        return switch (level == null ? "sedentary" : level.toLowerCase()) {
            case "lightly_active" -> 1.375;
            case "moderately_active" -> 1.55;
            case "very_active" -> 1.725;
            case "extra_active" -> 1.90;
            default -> 1.20;
        };
    }
}
