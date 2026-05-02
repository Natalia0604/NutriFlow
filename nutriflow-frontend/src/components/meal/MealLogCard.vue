<script setup lang="ts">
import type { MealLog } from '@/types'
import { MEAL_LABELS } from '@/utils/date'

const props = defineProps<{ log: MealLog }>()
defineEmits<{ delete: [number] }>()

const mealLabel = MEAL_LABELS[props.log.mealIndex] ?? '其他'
</script>

<template>
  <div class="card space-y-3">
    <div class="flex items-center justify-between">
      <div class="flex items-center gap-2">
        <span class="text-xs bg-primary-100 text-primary-600 font-semibold px-2 py-0.5 rounded-full">
          {{ mealLabel }}
        </span>
        <span v-if="log.storeName" class="text-xs text-gray-400">{{ log.storeName }}</span>
      </div>
      <div class="flex items-center gap-2">
        <span class="font-bold text-primary-500">{{ Math.round(log.totalKcal) }} kcal</span>
        <button class="text-gray-300 hover:text-red-400 text-sm transition-colors" @click="$emit('delete', log.id)">
          ✕
        </button>
      </div>
    </div>

    <div class="space-y-1">
      <div
        v-for="item in log.items"
        :key="item.id"
        class="flex items-center justify-between text-sm"
      >
        <span class="text-gray-700">{{ item.foodName }}</span>
        <span class="text-gray-400">{{ Math.round(item.kcal) }} kcal</span>
      </div>
    </div>

    <p v-if="log.note" class="text-xs text-gray-400 italic">{{ log.note }}</p>
  </div>
</template>
