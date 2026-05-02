import http from './http'
import type { ApiResponse, UserProfile, CatProfile, FoodPreference } from '@/types'

export const userService = {
  getProfile: () => http.get<ApiResponse<UserProfile>>('/users/me'),

  updateProfile: (data: Partial<UserProfile>) =>
    http.patch<ApiResponse<UserProfile>>('/users/me', data),

  getCat: () => http.get<ApiResponse<CatProfile>>('/users/me/cat'),

  updateCat: (data: Partial<CatProfile>) =>
    http.put<ApiResponse<CatProfile>>('/users/me/cat', data),

  getPreferences: () => http.get<ApiResponse<FoodPreference[]>>('/users/me/preferences'),

  addPreference: (data: { foodName: string; category: string; isCustom?: boolean; isAllergy?: boolean }) =>
    http.post<ApiResponse<FoodPreference>>('/users/me/preferences', data),

  syncPreferences: (list: Array<{ foodName: string; category: string; isCustom?: boolean; isAllergy?: boolean }>) =>
    http.put<ApiResponse<FoodPreference[]>>('/users/me/preferences', list),

  deletePreference: (id: number) =>
    http.delete<ApiResponse<void>>(`/users/me/preferences/${id}`)
}
