<script setup lang="ts">
import type { MealCell } from '@/types'
import { DAY_LABELS, getDayOfWeek } from '@/utils/date'
import MealCellCard from './MealCellCard.vue'

const props = defineProps<{
  dayIndex: number
  weekStart: string
  cells: MealCell[]
}>()
defineEmits<{ editCell: [MealCell]; logCell: [MealCell] }>()

const dayLabel = DAY_LABELS[props.dayIndex]
const dateLabel = getDayOfWeek(props.weekStart, props.dayIndex)
</script>

<template>
  <div class="min-w-[140px] space-y-2">
    <div class="text-center py-2 bg-white rounded-xl border border-orange-100 shadow-sm">
      <p class="text-xs font-bold text-gray-700">{{ dayLabel }}</p>
      <p class="text-xs text-gray-400">{{ dateLabel }}</p>
    </div>

    <MealCellCard
      v-for="cell in cells.filter(c => c.dayIndex === dayIndex)"
      :key="cell.id"
      :cell="cell"
      @edit="$emit('editCell', $event)"
      @log="$emit('logCell', $event)"
    />

    <div
      v-if="cells.filter(c => c.dayIndex === dayIndex).length === 0"
      class="rounded-xl border border-dashed border-gray-200 p-4 text-center text-xs text-gray-300"
    >
      尚無計畫
    </div>
  </div>
</template>
