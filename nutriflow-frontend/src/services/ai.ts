import http from './http'
import type { ApiResponse } from '@/types'

export const aiService = {
  analyzePhoto: (data: { imageBase64: string; mediaType?: string; storeName?: string; note?: string }) =>
    http.post<ApiResponse<{ items: any[] }>>('/ai/analyze-photo', data)
}
