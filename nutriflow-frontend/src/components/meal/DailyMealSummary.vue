<script setup lang="ts">
import type { MealLog } from '@/types'
import { MEAL_LABELS } from '@/utils/date'
import MealLogCard from './MealLogCard.vue'

defineProps<{ logs: MealLog[] }>()
defineEmits<{ delete: [number] }>()
</script>

<template>
  <div class="space-y-3">
    <div v-if="logs.length === 0" class="text-center py-8 text-gray-400 text-sm">
      今日尚未記錄任何餐點
    </div>
    <template v-else>
      <div
        v-for="mealIdx in [0, 1, 2]"
        :key="mealIdx"
      >
        <div
          v-if="logs.filter(l => l.mealIndex === mealIdx).length > 0"
          class="space-y-2"
        >
          <h3 class="text-sm font-semibold text-gray-500 px-1">{{ MEAL_LABELS[mealIdx] }}</h3>
          <MealLogCard
            v-for="log in logs.filter(l => l.mealIndex === mealIdx)"
            :key="log.id"
            :log="log"
            @delete="$emit('delete', $event)"
          />
        </div>
      </div>
    </template>
  </div>
</template>
