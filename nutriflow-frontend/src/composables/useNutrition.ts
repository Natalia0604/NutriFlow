import { useQuery } from '@tanstack/vue-query'
import { nutritionService } from '@/services/nutrition'
import { computed, type Ref } from 'vue'

export function useDailyNutrition(date: Ref<string>) {
  return useQuery({
    queryKey: computed(() => ['nutrition', 'daily', date.value]),
    queryFn: () => nutritionService.getDaily(date.value).then(r => r.data.data!)
  })
}

export function useWeeklyNutrition(weekStart: Ref<string>) {
  return useQuery({
    queryKey: computed(() => ['nutrition', 'weekly', weekStart.value]),
    queryFn: () => nutritionService.getWeekly(weekStart.value).then(r => r.data.data!)
  })
}
