import { useQuery, useMutation, useQueryClient } from '@tanstack/vue-query'
import { mealService } from '@/services/meal'
import { computed, type Ref } from 'vue'

export function useDailyMeals(date: Ref<string>) {
  const qc = useQueryClient()

  const query = useQuery({
    queryKey: computed(() => ['meals', 'daily', date.value]),
    queryFn: () => mealService.getDaily(date.value).then(r => r.data.data!)
  })

  const logMeal = useMutation({
    mutationFn: (data: any) => mealService.logMeal(data).then(r => r.data.data!),
    onSuccess: () => {
      qc.invalidateQueries({ queryKey: ['meals'] })
      qc.invalidateQueries({ queryKey: ['nutrition'] })
      qc.invalidateQueries({ queryKey: ['menu'] })
    }
  })

  const deleteLog = useMutation({
    mutationFn: (id: number) => mealService.deleteLog(id),
    onSuccess: () => {
      qc.invalidateQueries({ queryKey: ['meals'] })
      qc.invalidateQueries({ queryKey: ['nutrition'] })
      qc.invalidateQueries({ queryKey: ['menu'] })
    }
  })

  return { ...query, logMeal, deleteLog }
}
