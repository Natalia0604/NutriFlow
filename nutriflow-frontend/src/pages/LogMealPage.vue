<script setup lang="ts">
import { useRouter } from 'vue-router'
import { useDailyMeals } from '@/composables/useMeal'
import { today } from '@/utils/date'
import { ref } from 'vue'
import AppHeader from '@/components/base/AppHeader.vue'
import LogMealForm from '@/components/meal/LogMealForm.vue'
import { useToast } from '@/composables/useToast'

const router = useRouter()
const toast = useToast()
const date = ref(today())
const { logMeal } = useDailyMeals(date)

async function handleSubmit(data: any) {
  try {
    await logMeal.mutateAsync(data)
    toast.success('記錄成功！')
    router.push('/dashboard')
  } catch {
    toast.error('記錄失敗，請稍後再試')
  }
}
</script>

<template>
  <div>
    <AppHeader title="記錄餐點" :back="true" />
    <div class="p-4">
      <LogMealForm
        @submit="handleSubmit"
        @cancel="$router.back()"
      />
    </div>
  </div>
</template>
