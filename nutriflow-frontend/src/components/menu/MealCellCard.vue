<script setup lang="ts">
import type { MealCell } from '@/types'
import { MEAL_LABELS } from '@/utils/date'

const props = defineProps<{ cell: MealCell }>()
defineEmits<{ edit: [MealCell]; log: [MealCell] }>()
</script>

<template>
  <div
    class="rounded-xl border p-3 space-y-2 transition-colors"
    :class="cell.logged ? 'border-green-200 bg-green-50' : 'border-gray-100 bg-white'"
  >
    <div class="flex items-center justify-between">
      <span class="text-xs font-semibold text-gray-500">{{ MEAL_LABELS[cell.mealIndex] }}</span>
      <div class="flex items-center gap-1">
        <span v-if="cell.logged" class="text-xs text-green-500">✓</span>
        <span class="text-xs font-bold text-primary-500">{{ Math.round(cell.totalKcal) }} kcal</span>
      </div>
    </div>

    <div class="space-y-0.5">
      <p
        v-for="item in cell.items.slice(0, 3)"
        :key="item.id"
        class="text-xs text-gray-600 truncate"
      >
        {{ item.foodName }}
      </p>
      <p v-if="cell.items.length > 3" class="text-xs text-gray-400">
        +{{ cell.items.length - 3 }} 項
      </p>
    </div>

    <div v-if="cell.note" class="text-xs text-gray-400 italic truncate">{{ cell.note }}</div>

    <div class="flex gap-2 pt-1">
      <button class="flex-1 text-xs py-1 rounded-lg bg-gray-100 hover:bg-gray-200 text-gray-600 transition-colors" @click="$emit('edit', cell)">
        編輯
      </button>
      <button
        class="flex-1 text-xs py-1 rounded-lg transition-colors"
        :class="cell.logged ? 'bg-green-100 text-green-600' : 'bg-primary-100 text-primary-600 hover:bg-primary-200'"
        @click="$emit('log', cell)"
      >
        {{ cell.logged ? '已記錄' : '記錄' }}
      </button>
    </div>
  </div>
</template>
