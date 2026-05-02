import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { authService } from '@/services/auth'

export const useAuthStore = defineStore('auth', () => {
  const accessToken = ref<string | null>(null)
  const userId = ref<number | null>(null)
  const email = ref<string | null>(null)
  const isSetupComplete = ref(false)

  const isLoggedIn = computed(() => !!accessToken.value)

  function setAccessToken(token: string) {
    accessToken.value = token
  }

  function setAuthData(data: { accessToken: string; userId: number; email: string; isSetupComplete: boolean }) {
    accessToken.value = data.accessToken
    userId.value = data.userId
    email.value = data.email
    isSetupComplete.value = data.isSetupComplete
  }

  async function logout() {
    try { await authService.logout() } catch {}
    accessToken.value = null
    userId.value = null
    email.value = null
    isSetupComplete.value = false
  }

  return { accessToken, userId, email, isSetupComplete, isLoggedIn, setAccessToken, setAuthData, logout }
}, {
  persist: { pick: ['accessToken', 'userId', 'email', 'isSetupComplete'] }
})
