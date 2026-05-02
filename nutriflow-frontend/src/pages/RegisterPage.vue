<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { authService } from '@/services/auth'
import { useAuthStore } from '@/stores/auth'
import { useToast } from '@/composables/useToast'

const router = useRouter()
const auth = useAuthStore()
const toast = useToast()
const loading = ref(false)

const form = ref({
  email: '',
  password: '',
  gender: 'female',
  age: 25,
  weight: 60,
  height: 165,
  activityLevel: 'sedentary'
})

const activityOptions = [
  { value: 'sedentary', label: '久坐不動' },
  { value: 'lightly_active', label: '輕度活動' },
  { value: 'moderately_active', label: '中度活動' },
  { value: 'very_active', label: '高度活動' },
  { value: 'extra_active', label: '超高強度' }
]

async function register() {
  loading.value = true
  try {
    const res = await authService.register(form.value)
    auth.setAuthData(res.data.data!)
    router.push('/setup')
  } catch (e: any) {
    toast.error(e?.response?.data?.error?.message ?? '註冊失敗')
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="w-full max-w-sm space-y-4 py-4">
    <div class="text-center">
      <h1 class="text-2xl font-bold text-gray-800">建立帳號</h1>
      <p class="text-gray-500 text-sm">開始你的健康飲食旅程</p>
    </div>

    <div class="card space-y-3">
      <div>
        <label class="block text-sm font-medium text-gray-700 mb-1">Email</label>
        <input v-model="form.email" type="email" class="input" />
      </div>
      <div>
        <label class="block text-sm font-medium text-gray-700 mb-1">密碼（至少8字）</label>
        <input v-model="form.password" type="password" class="input" />
      </div>
      <div class="grid grid-cols-2 gap-3">
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">性別</label>
          <select v-model="form.gender" class="input">
            <option value="female">女</option>
            <option value="male">男</option>
          </select>
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">年齡</label>
          <input v-model.number="form.age" type="number" class="input" min="10" max="120" />
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">體重 (kg)</label>
          <input v-model.number="form.weight" type="number" step="0.1" class="input" />
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">身高 (cm)</label>
          <input v-model.number="form.height" type="number" step="0.1" class="input" />
        </div>
      </div>
      <div>
        <label class="block text-sm font-medium text-gray-700 mb-1">活動量</label>
        <select v-model="form.activityLevel" class="input">
          <option v-for="o in activityOptions" :key="o.value" :value="o.value">{{ o.label }}</option>
        </select>
      </div>

      <button class="btn-primary w-full" :disabled="loading" @click="register">
        {{ loading ? '建立中...' : '建立帳號' }}
      </button>
    </div>

    <p class="text-center text-sm text-gray-500">
      已有帳號？
      <RouterLink to="/login" class="text-primary-500 font-semibold hover:underline">登入</RouterLink>
    </p>
  </div>
</template>
