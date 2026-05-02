<script setup lang="ts">
import type { DailyNutrition } from '@/types'
import ProgressBar from '@/components/base/ProgressBar.vue'
import { kcalColor } from '@/utils/nutrition'

const props = defineProps<{ nutrition: DailyNutrition }>()
</script>

<template>
  <div class="card space-y-4">
    <div class="flex items-center justify-between">
      <h3 class="font-bold text-gray-800">今日攝取</h3>
      <span class="text-2xl font-bold" :class="kcalColor(nutrition.kcalProgress)">
        {{ Math.round(nutrition.totalKcal) }}
        <span class="text-sm font-normal text-gray-400">/ {{ Math.round(nutrition.targetKcal) }} kcal</span>
      </span>
    </div>

    <ProgressBar :value="nutrition.totalKcal" :max="nutrition.targetKcal" show-label />

    <div class="grid grid-cols-3 gap-3 text-center">
      <div class="bg-blue-50 rounded-xl p-3">
        <p class="text-xs text-blue-500 font-medium">蛋白質</p>
        <p class="text-lg font-bold text-blue-700">{{ Math.round(nutrition.totalProtein) }}g</p>
      </div>
      <div class="bg-yellow-50 rounded-xl p-3">
        <p class="text-xs text-yellow-600 font-medium">碳水</p>
        <p class="text-lg font-bold text-yellow-700">{{ Math.round(nutrition.totalCarbs) }}g</p>
      </div>
      <div class="bg-pink-50 rounded-xl p-3">
        <p class="text-xs text-pink-500 font-medium">脂肪</p>
        <p class="text-lg font-bold text-pink-700">{{ Math.round(nutrition.totalFat) }}g</p>
      </div>
    </div>
  </div>
</template>
