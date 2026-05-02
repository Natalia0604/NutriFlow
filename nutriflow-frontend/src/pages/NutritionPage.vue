<script setup lang="ts">
import { ref, computed } from 'vue'
import { getWeekStart } from '@/utils/date'
import { useDailyNutrition, useWeeklyNutrition } from '@/composables/useNutrition'
import { today } from '@/utils/date'
import AppHeader from '@/components/base/AppHeader.vue'
import MacroDonutChart from '@/components/charts/MacroDonutChart.vue'
import WeeklyKcalChart from '@/components/charts/WeeklyKcalChart.vue'
import NutritionSummaryCard from '@/components/charts/NutritionSummaryCard.vue'
import LoadingSpinner from '@/components/base/LoadingSpinner.vue'

const date = ref(today())
const weekStart = ref(getWeekStart())

const { data: daily, isLoading: loadingDaily } = useDailyNutrition(date)
const { data: weekly, isLoading: loadingWeekly } = useWeeklyNutrition(weekStart)
</script>

<template>
  <div>
    <AppHeader title="營養分析" />
    <div class="p-4 space-y-4">
      <input v-model="date" type="date" class="input" />

      <LoadingSpinner v-if="loadingDaily" />
      <MacroDonutChart v-else-if="daily" :nutrition="daily" />

      <LoadingSpinner v-if="loadingWeekly" />
      <template v-else-if="weekly">
        <WeeklyKcalChart :weekly="weekly" />
        <NutritionSummaryCard :weekly="weekly" />
      </template>
    </div>
  </div>
</template>
