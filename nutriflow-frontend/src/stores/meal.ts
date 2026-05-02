import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { MealLog } from '@/types'
import dayjs from 'dayjs'

export const useMealStore = defineStore('meal', () => {
  const dailyLogs = ref<MealLog[]>([])
  const selectedDate = ref<string>(dayjs().format('YYYY-MM-DD'))

  function setDailyLogs(logs: MealLog[]) { dailyLogs.value = logs }
  function setSelectedDate(date: string) { selectedDate.value = date }

  return { dailyLogs, selectedDate, setDailyLogs, setSelectedDate }
})
