import http from './http'
import type { ApiResponse, AuthResponse } from '@/types'

export const authService = {
  register: (data: {
    email: string; password: string; gender: string; age: number
    weight: number; height: number; activityLevel: string
  }) => http.post<ApiResponse<AuthResponse>>('/auth/register', data),

  login: (data: { email: string; password: string }) =>
    http.post<ApiResponse<AuthResponse>>('/auth/login', data),

  refresh: () => http.post<ApiResponse<AuthResponse>>('/auth/refresh'),

  logout: () => http.post<ApiResponse<void>>('/auth/logout')
}
