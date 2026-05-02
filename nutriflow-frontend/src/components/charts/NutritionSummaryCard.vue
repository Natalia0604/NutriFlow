<script setup lang="ts">
import type { WeeklyNutrition } from '@/types'

defineProps<{ weekly: WeeklyNutrition }>()
</script>

<template>
  <div class="card space-y-3">
    <h3 class="font-bold text-gray-800">本週平均</h3>
    <div class="grid grid-cols-2 gap-3">
      <div class="bg-orange-50 rounded-xl p-3 text-center">
        <p class="text-xs text-orange-500 font-medium">熱量</p>
        <p class="text-xl font-bold text-orange-600">{{ Math.round(weekly.avgKcal) }}</p>
        <p class="text-xs text-gray-400">kcal / 天</p>
      </div>
      <div class="bg-blue-50 rounded-xl p-3 text-center">
        <p class="text-xs text-blue-500 font-medium">蛋白質</p>
        <p class="text-xl font-bold text-blue-600">{{ Math.round(weekly.avgProtein) }}</p>
        <p class="text-xs text-gray-400">g / 天</p>
      </div>
      <div class="bg-yellow-50 rounded-xl p-3 text-center">
        <p class="text-xs text-yellow-600 font-medium">碳水</p>
        <p class="text-xl font-bold text-yellow-700">{{ Math.round(weekly.avgCarbs) }}</p>
        <p class="text-xs text-gray-400">g / 天</p>
      </div>
      <div class="bg-pink-50 rounded-xl p-3 text-center">
        <p class="text-xs text-pink-500 font-medium">脂肪</p>
        <p class="text-xl font-bold text-pink-600">{{ Math.round(weekly.avgFat) }}</p>
        <p class="text-xs text-gray-400">g / 天</p>
      </div>
    </div>
  </div>
</template>
