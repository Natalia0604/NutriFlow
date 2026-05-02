<script setup lang="ts">
import { computed } from 'vue'
import { Line } from 'vue-chartjs'
import { Chart as ChartJS, CategoryScale, LinearScale, PointElement, LineElement, Title, Tooltip, Filler } from 'chart.js'
import type { WeightRecord } from '@/types'

ChartJS.register(CategoryScale, LinearScale, PointElement, LineElement, Title, Tooltip, Filler)

const props = defineProps<{ records: WeightRecord[]; targetWeight?: number }>()

const sorted = computed(() => [...props.records].sort((a, b) => a.recordDate.localeCompare(b.recordDate)))

const data = computed(() => ({
  labels: sorted.value.map(r => r.recordDate.slice(5)),
  datasets: [
    {
      label: '體重 (kg)',
      data: sorted.value.map(r => r.weight),
      borderColor: '#F97316',
      backgroundColor: 'rgba(249,115,22,0.08)',
      fill: true,
      tension: 0.4,
      pointRadius: 4
    },
    ...(props.targetWeight ? [{
      label: '目標 (kg)',
      data: sorted.value.map(() => props.targetWeight),
      borderColor: '#94A3B8',
      borderDash: [6, 3],
      pointRadius: 0,
      fill: false
    }] : [])
  ]
}))

const options = {
  responsive: true,
  plugins: { legend: { position: 'bottom' as const } },
  scales: { y: { beginAtZero: false } }
}
</script>

<template>
  <div class="card">
    <h3 class="font-bold text-gray-800 mb-3">體重趨勢</h3>
    <Line :data="data" :options="options" />
  </div>
</template>
