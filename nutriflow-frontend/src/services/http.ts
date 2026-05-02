import axios from 'axios'
import type { AxiosInstance } from 'axios'
import { useAuthStore } from '@/stores/auth'
import router from '@/router'

const http: AxiosInstance = axios.create({
  baseURL: (import.meta.env.VITE_API_BASE_URL as string) || '/api',
  timeout: 30000,
  withCredentials: true
})

http.interceptors.request.use((config) => {
  const auth = useAuthStore()
  if (auth.accessToken) {
    config.headers.Authorization = `Bearer ${auth.accessToken}`
  }
  return config
})

let isRefreshing = false
let refreshQueue: Array<(token: string) => void> = []

http.interceptors.response.use(
  (res) => res,
  async (error) => {
    const original = error.config
    if (error.response?.status === 401 && !original._retry) {
      if (isRefreshing) {
        return new Promise((resolve) => {
          refreshQueue.push((token) => {
            original.headers.Authorization = `Bearer ${token}`
            resolve(http(original))
          })
        })
      }
      original._retry = true
      isRefreshing = true
      try {
        const auth = useAuthStore()
        const baseURL = (import.meta.env.VITE_API_BASE_URL as string) || '/api'
        const res = await axios.post(`${baseURL}/auth/refresh`, {}, { withCredentials: true })
        const { accessToken } = res.data.data
        auth.setAccessToken(accessToken)
        refreshQueue.forEach((cb) => cb(accessToken))
        refreshQueue = []
        original.headers.Authorization = `Bearer ${accessToken}`
        return http(original)
      } catch {
        useAuthStore().logout()
        router.push('/login')
        return Promise.reject(error)
      } finally {
        isRefreshing = false
      }
    }
    return Promise.reject(error)
  }
)

export default http
