import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { DailyNutrition } from '@/types'

export const useNutritionStore = defineStore('nutrition', () => {
  const today = ref<DailyNutrition | null>(null)

  function setToday(data: DailyNutrition) { today.value = data }

  return { today, setToday }
})
