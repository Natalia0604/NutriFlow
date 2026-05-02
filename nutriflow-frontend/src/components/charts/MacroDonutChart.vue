<script setup lang="ts">
import { computed } from 'vue'
import { Doughnut } from 'vue-chartjs'
import { Chart as ChartJS, ArcElement, Tooltip, Legend } from 'chart.js'
import type { DailyNutrition } from '@/types'

ChartJS.register(ArcElement, Tooltip, Legend)

const props = defineProps<{ nutrition: DailyNutrition }>()

const data = computed(() => ({
  labels: ['蛋白質', '碳水', '脂肪'],
  datasets: [{
    data: [
      Math.round(props.nutrition.totalProtein * 4),
      Math.round(props.nutrition.totalCarbs * 4),
      Math.round(props.nutrition.totalFat * 9)
    ],
    backgroundColor: ['#60A5FA', '#FCD34D', '#F9A8D4'],
    borderWidth: 0
  }]
}))

const options = {
  responsive: true,
  cutout: '65%',
  plugins: { legend: { position: 'bottom' as const } }
}
</script>

<template>
  <div class="card">
    <h3 class="font-bold text-gray-800 mb-3">三大營養素佔比</h3>
    <Doughnut :data="data" :options="options" />
  </div>
</template>
