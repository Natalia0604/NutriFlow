import http from './http'
import type { ApiResponse, WeekMenu, MealCell } from '@/types'

export const menuService = {
  getWeekMenu: (weekStart: string) =>
    http.get<ApiResponse<WeekMenu>>('/menus', { params: { weekStart } }),

  generateMenu: (data: { weekStart: string; extraNote?: string; forceRegenerate?: boolean }) =>
    http.post<ApiResponse<WeekMenu>>('/menus/generate', data),

  createBlankMenu: (data: { weekStart: string }) =>
    http.post<ApiResponse<WeekMenu>>('/menus/blank', data),

  updateCell: (menuId: number, data: {
    dayIndex: number; mealIndex: number; mealType?: string
    logTime?: string; note?: string; items?: any[]
  }) => http.put<ApiResponse<MealCell>>(`/menus/${menuId}/cells`, data)
}
