<script setup lang="ts">
import dayjs from 'dayjs'
import { getWeekStart } from '@/utils/date'

const props = defineProps<{ modelValue: string }>()
const emit = defineEmits<{ 'update:modelValue': [string] }>()

function prev() {
  emit('update:modelValue', dayjs(props.modelValue).subtract(7, 'day').format('YYYY-MM-DD'))
}
function next() {
  emit('update:modelValue', dayjs(props.modelValue).add(7, 'day').format('YYYY-MM-DD'))
}
function thisWeek() {
  emit('update:modelValue', getWeekStart())
}
</script>

<template>
  <div class="flex items-center justify-between bg-white rounded-2xl px-4 py-3 shadow-sm border border-orange-100">
    <button class="text-gray-400 hover:text-gray-600 text-xl px-2" @click="prev">‹</button>
    <div class="text-center">
      <p class="text-sm font-bold text-gray-800">
        {{ dayjs(modelValue).format('YYYY年 MM/DD') }} —
        {{ dayjs(modelValue).add(6, 'day').format('MM/DD') }}
      </p>
      <button class="text-xs text-primary-500 hover:underline" @click="thisWeek">本週</button>
    </div>
    <button class="text-gray-400 hover:text-gray-600 text-xl px-2" @click="next">›</button>
  </div>
</template>
