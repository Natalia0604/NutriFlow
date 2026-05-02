<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { authService } from '@/services/auth'
import { useAuthStore } from '@/stores/auth'
import { useToast } from '@/composables/useToast'
import CatMascot from '@/components/cat/CatMascot.vue'

const router = useRouter()
const auth = useAuthStore()
const toast = useToast()

const email = ref('')
const password = ref('')
const loading = ref(false)

async function login() {
  loading.value = true
  try {
    const res = await authService.login({ email: email.value, password: password.value })
    auth.setAuthData(res.data.data!)
    router.push(auth.isSetupComplete ? '/dashboard' : '/setup')
  } catch (e: any) {
    toast.error(e?.response?.data?.error?.message ?? '登入失敗')
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="w-full max-w-sm space-y-6">
    <div class="text-center space-y-2">
      <CatMascot mood="happy" size="lg" />
      <h1 class="text-2xl font-bold text-gray-800">NutriFlow</h1>
      <p class="text-gray-500 text-sm">貓咪陪你吃健康 🐱</p>
    </div>

    <div class="card space-y-4">
      <div>
        <label class="block text-sm font-medium text-gray-700 mb-1">Email</label>
        <input v-model="email" type="email" class="input" placeholder="you@example.com" @keydown.enter="login" />
      </div>
      <div>
        <label class="block text-sm font-medium text-gray-700 mb-1">密碼</label>
        <input v-model="password" type="password" class="input" placeholder="••••••••" @keydown.enter="login" />
      </div>
      <button class="btn-primary w-full" :disabled="loading" @click="login">
        {{ loading ? '登入中...' : '登入' }}
      </button>
    </div>

    <p class="text-center text-sm text-gray-500">
      還沒有帳號？
      <RouterLink to="/register" class="text-primary-500 font-semibold hover:underline">立即註冊</RouterLink>
    </p>
  </div>
</template>
