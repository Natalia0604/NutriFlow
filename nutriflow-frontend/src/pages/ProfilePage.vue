<script setup lang="ts">
import { ref } from 'vue'
import { useProfile } from '@/composables/useProfile'
import { useAuthStore } from '@/stores/auth'
import { useRouter } from 'vue-router'
import AppHeader from '@/components/base/AppHeader.vue'
import LoadingSpinner from '@/components/base/LoadingSpinner.vue'
import { useToast } from '@/composables/useToast'

const auth = useAuthStore()
const router = useRouter()
const toast = useToast()
const { data: profile, isLoading, update } = useProfile()
const editing = ref(false)
const saving = ref(false)

const form = ref({
  age: 0, weight: 0, height: 0, activityLevel: '', hasWeightGoal: false, targetWeight: 0, goalWeeks: 12
})

function startEdit() {
  if (!profile.value) return
  form.value = {
    age: profile.value.age,
    weight: Number(profile.value.weight),
    height: Number(profile.value.height),
    activityLevel: profile.value.activityLevel,
    hasWeightGoal: profile.value.hasWeightGoal,
    targetWeight: Number(profile.value.targetWeight ?? 0),
    goalWeeks: profile.value.goalWeeks ?? 12
  }
  editing.value = true
}

async function save() {
  saving.value = true
  try {
    await update.mutateAsync(form.value)
    editing.value = false
    toast.success('已更新')
  } catch {
    toast.error('更新失敗')
  } finally {
    saving.value = false
  }
}

async function logout() {
  await auth.logout()
  router.push('/login')
}
</script>

<template>
  <div>
    <AppHeader title="個人資料" />
    <div class="p-4 space-y-4">
      <LoadingSpinner v-if="isLoading" />
      <div v-else-if="!profile" class="card text-center text-gray-400 text-sm py-6">
        無法載入個人資料
        <button class="block mx-auto mt-4 text-red-500 font-medium" @click="logout">登出</button>
      </div>
      <template v-else>
        <div class="card space-y-3" v-if="!editing">
          <div class="grid grid-cols-2 gap-3 text-sm">
            <div><span class="text-gray-400">Email：</span><span class="font-medium">{{ profile.email }}</span></div>
            <div><span class="text-gray-400">性別：</span><span class="font-medium">{{ profile.gender === 'female' ? '女' : '男' }}</span></div>
            <div><span class="text-gray-400">年齡：</span><span class="font-medium">{{ profile.age }} 歲</span></div>
            <div><span class="text-gray-400">體重：</span><span class="font-medium">{{ profile.weight }} kg</span></div>
            <div><span class="text-gray-400">身高：</span><span class="font-medium">{{ profile.height }} cm</span></div>
            <div><span class="text-gray-400">TDEE：</span><span class="font-medium text-primary-500">{{ Math.round(profile.tdee) }} kcal</span></div>
          </div>
          <button class="btn-secondary w-full" @click="startEdit">編輯資料</button>
        </div>

        <div class="card space-y-3" v-else>
          <div class="grid grid-cols-2 gap-3">
            <div>
              <label class="block text-xs font-medium text-gray-600 mb-1">年齡</label>
              <input v-model.number="form.age" type="number" class="input" />
            </div>
            <div>
              <label class="block text-xs font-medium text-gray-600 mb-1">體重 (kg)</label>
              <input v-model.number="form.weight" type="number" step="0.1" class="input" />
            </div>
          </div>
          <label class="flex items-center gap-2 text-sm">
            <input v-model="form.hasWeightGoal" type="checkbox" />
            有體重目標
          </label>
          <template v-if="form.hasWeightGoal">
            <input v-model.number="form.targetWeight" type="number" step="0.5" class="input" placeholder="目標體重 (kg)" />
            <input v-model.number="form.goalWeeks" type="number" class="input" placeholder="計畫週數" />
          </template>
          <div class="flex gap-3">
            <button class="btn-secondary flex-1" @click="editing = false">取消</button>
            <button class="btn-primary flex-1" :disabled="saving" @click="save">{{ saving ? '儲存中...' : '儲存' }}</button>
          </div>
        </div>

        <RouterLink to="/cat" class="btn-secondary w-full flex items-center justify-center gap-2">
          🐱 貓咪設定
        </RouterLink>
        <RouterLink to="/weight" class="btn-secondary w-full flex items-center justify-center gap-2">
          ⚖️ 體重追蹤
        </RouterLink>

        <button class="w-full py-3 text-red-500 text-sm font-medium hover:bg-red-50 rounded-xl transition-colors" @click="logout">
          登出
        </button>
      </template>
    </div>
  </div>
</template>
