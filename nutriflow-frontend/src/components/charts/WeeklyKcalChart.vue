<script setup lang="ts">
import { computed } from 'vue'
import { Bar } from 'vue-chartjs'
import { Chart as ChartJS, CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend } from 'chart.js'
import type { WeeklyNutrition } from '@/types'
import { DAY_LABELS } from '@/utils/date'

ChartJS.register(CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend)

const props = defineProps<{ weekly: WeeklyNutrition }>()

const data = computed(() => ({
  labels: props.weekly.days.map((_, i) => DAY_LABELS[i]),
  datasets: [
    {
      label: '攝取 (kcal)',
      data: props.weekly.days.map(d => Math.round(d.totalKcal)),
      backgroundColor: 'rgba(249, 115, 22, 0.7)',
      borderRadius: 8
    },
    {
      label: '目標 (kcal)',
      data: props.weekly.days.map(() => Math.round(props.weekly.targetKcal)),
      backgroundColor: 'rgba(156, 163, 175, 0.3)',
      borderRadius: 8
    }
  ]
}))

const options = {
  responsive: true,
  plugins: { legend: { position: 'bottom' as const } },
  scales: { y: { beginAtZero: true } }
}
</script>

<template>
  <div class="card">
    <h3 class="font-bold text-gray-800 mb-3">本週熱量趨勢</h3>
    <Bar :data="data" :options="options" />
  </div>
</template>
