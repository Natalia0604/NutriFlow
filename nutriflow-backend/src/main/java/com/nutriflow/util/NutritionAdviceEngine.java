package com.nutriflow.util;

import org.springframework.stereotype.Component;

@Component
public class NutritionAdviceEngine {

    public String generateDailyAdvice(double totalKcal, double targetKcal, double protein, double carbs, double fat) {
        double ratio = totalKcal / targetKcal;
        if (ratio < 0.7) return "今日攝取熱量偏低，記得補充足夠營養喔！";
        if (ratio > 1.2) return "今日熱量超標，明天可以適度減少高熱量食物。";
        if (protein < 50) return "蛋白質攝取不足，可以多吃豆腐、雞胸肉或蛋。";
        if (fat > totalKcal * 0.4 / 9) return "脂肪比例偏高，建議選擇低脂烹調方式。";
        return "今日飲食均衡，繼續保持！";
    }

    public String generateWeeklyAdvice(double avgKcal, double targetKcal) {
        double ratio = avgKcal / targetKcal;
        if (ratio < 0.8) return "本週平均攝取熱量偏低，請確保飲食多樣性。";
        if (ratio > 1.15) return "本週平均熱量超標，建議增加蔬菜比例並減少精緻糖。";
        return "本週飲食控制良好，繼續維持！";
    }
}
