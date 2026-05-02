import http from './http'
import type { ApiResponse, WeightRecord } from '@/types'

export const weightService = {
  getHistory: (limit = 30) =>
    http.get<ApiResponse<WeightRecord[]>>('/weights', { params: { limit } }),

  logWeight: (data: { weight: number; recordDate: string; note?: string }) =>
    http.post<ApiResponse<WeightRecord>>('/weights', data),

  deleteRecord: (id: number) => http.delete<ApiResponse<void>>(`/weights/${id}`)
}
