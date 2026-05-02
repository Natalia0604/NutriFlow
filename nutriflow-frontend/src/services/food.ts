import http from './http'
import type { ApiResponse, FoodSearchResult, CustomFood, SaveCustomFoodRequest } from '@/types'

export const foodService = {
  search: (query: string, store?: string) =>
    http.get<ApiResponse<FoodSearchResult[]>>('/foods/search', { params: { query, store } }),

  listCustomFoods: () =>
    http.get<ApiResponse<CustomFood[]>>('/foods/mine'),

  createCustomFood: (data: SaveCustomFoodRequest) =>
    http.post<ApiResponse<CustomFood>>('/foods/mine', data),

  updateCustomFood: (id: number, data: SaveCustomFoodRequest) =>
    http.put<ApiResponse<CustomFood>>(`/foods/mine/${id}`, data),

  deleteCustomFood: (id: number) =>
    http.delete<ApiResponse<void>>(`/foods/mine/${id}`)
}
