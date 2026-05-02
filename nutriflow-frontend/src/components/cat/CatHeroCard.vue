<script setup lang="ts">
import { computed } from 'vue'
import CatSvg from './CatSvg.vue'
import type { CatBreed } from '@/constants/CatBreeds'
import { CAT_BREED_NAMES } from '@/constants/CatBreeds'
import type { DailyNutrition } from '@/types'

const props = defineProps<{
  catName?: string
  catBreed?: CatBreed | string
  userName?: string
  nutrition?: DailyNutrition | null
}>()

const catDisplayName = computed(() =>
  props.catName || CAT_BREED_NAMES[(props.catBreed as CatBreed) ?? 'orange'] || '小橘'
)

const mood = computed(() => {
  const p = props.nutrition?.kcalProgress ?? 0
  if (p >= 90 && p <= 110) return '超棒'
  if (p >= 70) return '不錯'
  if (p === 0) return '等你記錄'
  return '需補充'
})

const moodEmoji = computed(() => {
  const p = props.nutrition?.kcalProgress ?? 0
  if (p >= 90 && p <= 110) return '😻'
  if (p >= 70) return '😸'
  if (p === 0) return '😺'
  return '😿'
})

const advice = computed(() => props.nutrition?.advice ?? '記錄今天的飲食，讓我幫你分析！🐾')

// Ring calculation (circumference = 2π×30 ≈ 188)
const CIRC = 188
const dashOffset = computed(() => {
  const pct = Math.min((props.nutrition?.kcalProgress ?? 0) / 100, 1)
  return CIRC - CIRC * pct
})

const ringColor = computed(() => {
  const p = props.nutrition?.kcalProgress ?? 0
  if (p > 110) return '#FF85A1'
  if (p >= 80) return '#FF8C42'
  return '#FF8C42'
})
</script>

<template>
  <div class="cat-hero mx-4 mt-4 rounded-[2rem] p-6 shadow-md border border-orange-100/50 overflow-hidden relative"
    style="background:linear-gradient(145deg,#FFF0E6,#FFE4D0)">

    <!-- Header -->
    <div class="flex items-start justify-between mb-2">
      <div>
        <p class="text-xs font-semibold text-amber-700/70">你好，{{ userName || '朋友' }} 👋</p>
        <h2 class="text-xl font-black text-amber-900 tracking-tight">{{ catDisplayName }}今天{{ mood === '超棒' ? '很滿足' : mood === '等你記錄' ? '在等你' : '需要你' }}</h2>
        <span class="inline-block mt-1 px-3 py-0.5 bg-emerald-200/70 text-emerald-800 text-xs font-black rounded-full">
          {{ moodEmoji }} 心情：{{ mood }}
        </span>
      </div>
    </div>

    <!-- Cat + Speech bubble -->
    <div class="flex items-end gap-4 my-3">
      <CatSvg :breed="catBreed" :animated="true" :size="110" />

      <div class="speech-bubble flex-1 bg-white rounded-[18px_18px_18px_4px] px-3.5 py-2.5 text-xs font-bold text-gray-700 shadow-sm border-2 border-gray-100 leading-relaxed relative">
        {{ advice }}
      </div>
    </div>

    <!-- Daily ring row -->
    <div v-if="nutrition" class="flex items-center gap-4 bg-white/70 rounded-2xl p-3.5 mt-1">
      <!-- SVG Ring -->
      <div class="relative w-[72px] h-[72px] flex-shrink-0">
        <svg width="72" height="72" viewBox="0 0 72 72" style="transform:rotate(-90deg)">
          <circle fill="none" stroke="#EDE0D8" stroke-width="7" cx="36" cy="36" r="30"/>
          <circle
            fill="none"
            :stroke="ringColor"
            stroke-width="7"
            stroke-linecap="round"
            cx="36" cy="36" r="30"
            :stroke-dasharray="CIRC"
            :stroke-dashoffset="dashOffset"
            style="transition:stroke-dashoffset 1s ease"
          />
        </svg>
        <div class="absolute inset-0 flex flex-col items-center justify-center">
          <span class="text-[13px] font-black text-orange-600 leading-none">{{ Math.round(nutrition.totalKcal) }}</span>
          <span class="text-[9px] text-gray-400 font-bold mt-0.5">/ {{ Math.round(nutrition.targetKcal) }}</span>
        </div>
      </div>

      <!-- Macro bars -->
      <div class="flex-1">
        <p class="text-[13px] font-bold text-gray-800 mb-1.5">今日熱量 {{ Math.round(nutrition.kcalProgress) }}%</p>
        <div class="space-y-1">
          <div class="flex items-center gap-2">
            <span class="text-[11px] w-5">💪</span>
            <div class="flex-1 h-[7px] bg-gray-100 rounded-full overflow-hidden">
              <div class="h-full rounded-full bg-emerald-400 transition-all duration-700"
                :style="`width:${Math.min((nutrition.totalProtein / 60) * 100, 100)}%`" />
            </div>
            <span class="text-[11px] text-gray-500 font-bold w-8 text-right">{{ Math.round(nutrition.totalProtein) }}g</span>
          </div>
          <div class="flex items-center gap-2">
            <span class="text-[11px] w-5">🌾</span>
            <div class="flex-1 h-[7px] bg-gray-100 rounded-full overflow-hidden">
              <div class="h-full rounded-full bg-yellow-400 transition-all duration-700"
                :style="`width:${Math.min((nutrition.totalCarbs / 200) * 100, 100)}%`" />
            </div>
            <span class="text-[11px] text-gray-500 font-bold w-8 text-right">{{ Math.round(nutrition.totalCarbs) }}g</span>
          </div>
          <div class="flex items-center gap-2">
            <span class="text-[11px] w-5">🧈</span>
            <div class="flex-1 h-[7px] bg-gray-100 rounded-full overflow-hidden">
              <div class="h-full rounded-full bg-pink-400 transition-all duration-700"
                :style="`width:${Math.min((nutrition.totalFat / 65) * 100, 100)}%`" />
            </div>
            <span class="text-[11px] text-gray-500 font-bold w-8 text-right">{{ Math.round(nutrition.totalFat) }}g</span>
          </div>
        </div>
      </div>
    </div>

    <!-- Loading state for ring -->
    <div v-else class="flex items-center gap-3 bg-white/70 rounded-2xl p-3.5 mt-1">
      <div class="w-[72px] h-[72px] flex-shrink-0 flex items-center justify-center rounded-full bg-orange-100/50">
        <span class="text-2xl">🐾</span>
      </div>
      <p class="text-sm text-gray-500 font-medium">記錄今天的餐點<br>讓我幫你計算熱量！</p>
    </div>
  </div>
</template>

<style scoped>
.speech-bubble::before {
  content: '';
  position: absolute;
  left: -10px;
  bottom: 14px;
  border: 6px solid transparent;
  border-right-color: #e5e7eb;
}
.speech-bubble::after {
  content: '';
  position: absolute;
  left: -7px;
  bottom: 15px;
  border: 5px solid transparent;
  border-right-color: white;
}
</style>
