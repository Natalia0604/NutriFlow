<script setup lang="ts">
import { computed } from 'vue'
import { kcalBarColor } from '@/utils/nutrition'

const props = defineProps<{ value: number; max: number; showLabel?: boolean }>()

const pct = computed(() => Math.min((props.value / props.max) * 100, 100))
const barColor = computed(() => kcalBarColor((props.value / props.max) * 100))
</script>

<template>
  <div class="space-y-1">
    <div class="flex justify-between text-xs text-gray-500" v-if="showLabel">
      <span>{{ Math.round(value) }}</span>
      <span>{{ Math.round(max) }}</span>
    </div>
    <div class="h-2 bg-gray-100 rounded-full overflow-hidden">
      <div
        class="h-full rounded-full transition-all duration-500"
        :class="barColor"
        :style="{ width: `${pct}%` }"
      />
    </div>
  </div>
</template>
