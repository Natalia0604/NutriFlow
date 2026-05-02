import http from './http'
import type { ApiResponse, MealLog, PageResponse } from '@/types'

export const mealService = {
  getDaily: (date: string) =>
    http.get<ApiResponse<MealLog[]>>('/meals/daily', { params: { date } }),

  getHistory: (page = 0, size = 20) =>
    http.get<ApiResponse<PageResponse<MealLog>>>('/meals/history', { params: { page, size } }),

  logMeal: (data: {
    mealDate: string; mealIndex: number; mealType?: string
    mealCellId?: number; storeName?: string; note?: string; items?: any[]
  }) => http.post<ApiResponse<MealLog>>('/meals', data),

  deleteLog: (id: number) => http.delete<ApiResponse<void>>(`/meals/${id}`)
}
