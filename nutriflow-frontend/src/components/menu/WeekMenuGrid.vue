<script setup lang="ts">
import { computed } from 'vue'
import { dayjs } from '@/utils/date'
import type { MealCell } from '@/types'

const props = defineProps<{
  cells: MealCell[]
  weekStart: string
  todayStr: string
}>()

const emit = defineEmits<{
  cellClick: [{ dayIndex: number; mealIndex: number; cell: MealCell | null }]
}>()

const DAY_COLS  = ['一', '二', '三', '四', '五', '六', '日']
const MEAL_ROWS = ['早', '午', '晚']

const FOOD_EMOJIS = [
  ['🍳', '🥞', '🥐', '🥗', '🍵', '🧇', '🥣'],
  ['🍱', '🍜', '🍛', '🥗', '🥙', '🍲', '🌮'],
  ['🍲', '🥩', '🐟', '🥦', '🍛', '🥗', '🫕'],
]

// Pre-compute 3×7 grid to avoid calling find() repeatedly in template
const grid = computed(() =>
  [0, 1, 2].map(meal =>
    [0, 1, 2, 3, 4, 5, 6].map(day =>
      props.cells.find(c => c.dayIndex === day && c.mealIndex === meal) ?? null
    )
  )
)

function isToday(dayIndex: number) {
  return dayjs(props.weekStart).add(dayIndex, 'day').format('YYYY-MM-DD') === props.todayStr
}

function cellClass(cell: MealCell | null, dayIndex: number) {
  if (isToday(dayIndex))
    return 'border-orange-400 bg-orange-50 shadow-sm'
  if (!cell || cell.items.length === 0)
    return 'border-dashed border-gray-200 bg-white/60'
  if (cell.logged)
    return 'border-emerald-400 bg-emerald-50 shadow-sm'
  if (cell.mealType === '自炊')
    return 'border-emerald-300 bg-emerald-50/60'
  return 'border-teal-200 bg-white'
}
</script>

<template>
  <div class="select-none w-full">
    <!-- Column headers -->
    <div class="grid mb-1" style="grid-template-columns: 18px repeat(7, 1fr); gap: 3px">
      <div />
      <div
        v-for="(label, i) in DAY_COLS"
        :key="i"
        class="text-center text-[11px] font-black py-0.5 rounded"
        :class="isToday(i) ? 'text-orange-500' : 'text-gray-400'"
      >{{ label }}</div>
    </div>

    <!-- 3 meal rows -->
    <div
      v-for="(mealLabel, mealIndex) in MEAL_ROWS"
      :key="mealIndex"
      class="grid mb-[3px]"
      style="grid-template-columns: 18px repeat(7, 1fr); gap: 3px"
    >
      <!-- Row label -->
      <div class="flex items-center justify-center text-[11px] font-black text-gray-400 leading-none">
        {{ mealLabel }}
      </div>

      <!-- 7 day cells -->
      <div
        v-for="(cell, dayIndex) in grid[mealIndex]"
        :key="dayIndex"
        class="rounded-xl border-2 cursor-pointer transition-all active:scale-95 flex flex-col items-center justify-center py-1"
        style="min-height: 62px"
        :class="cellClass(cell, dayIndex)"
        @click="emit('cellClick', { dayIndex, mealIndex, cell })"
      >
        <!-- Filled cell -->
        <template v-if="cell && cell.items.length > 0">
          <span class="text-[18px] leading-none">{{ FOOD_EMOJIS[mealIndex][dayIndex] }}</span>
          <span class="text-[10px] font-black text-gray-700 mt-0.5 leading-none">
            {{ Math.round(cell.totalKcal) }}k
          </span>
          <span class="text-[9px] text-gray-400 leading-none mt-[2px]">
            +{{ cell.items.length }}項
          </span>
        </template>

        <!-- Cell set to 自炊/外食 but no items yet -->
        <template v-else-if="cell?.mealType === '自炊' || cell?.mealType === '外食'">
          <span
            class="text-[10px] font-bold"
            :class="cell.mealType === '自炊' ? 'text-emerald-500' : 'text-teal-400'"
          >{{ cell.mealType }}</span>
        </template>

        <!-- Empty cell -->
        <template v-else>
          <span class="text-gray-300 text-[22px] font-extralight leading-none">+</span>
        </template>
      </div>
    </div>
  </div>
</template>
