import http from './http'
import type { ApiResponse, DailyNutrition, WeeklyNutrition } from '@/types'

export const nutritionService = {
  getDaily: (date?: string) =>
    http.get<ApiResponse<DailyNutrition>>('/nutrition/daily', { params: date ? { date } : {} }),

  getWeekly: (weekStart: string) =>
    http.get<ApiResponse<WeeklyNutrition>>('/nutrition/weekly', { params: { weekStart } })
}
