<script setup lang="ts">
import { ref } from 'vue'

const emit = defineEmits<{ save: [{ knucklePxRatio: number; bowlWidthMm: number }]; cancel: [] }>()

const knucklePx = ref('')
const bowlMm = ref('')

function save() {
  emit('save', {
    knucklePxRatio: parseFloat(knucklePx.value),
    bowlWidthMm: parseFloat(bowlMm.value)
  })
}
</script>

<template>
  <div class="card space-y-4">
    <h3 class="font-bold text-gray-800">碗口校正</h3>
    <p class="text-sm text-gray-500">拍攝您的拳頭與貓碗，輸入像素比例以精準計算食物份量。</p>

    <div>
      <label class="block text-sm font-medium text-gray-700 mb-1">拳頭像素比例 (px/mm)</label>
      <input v-model="knucklePx" type="number" step="0.0001" class="input" placeholder="e.g. 3.1415" />
    </div>
    <div>
      <label class="block text-sm font-medium text-gray-700 mb-1">碗口寬度 (mm)</label>
      <input v-model="bowlMm" type="number" step="0.1" class="input" placeholder="e.g. 120.5" />
    </div>

    <div class="flex gap-3">
      <button class="btn-secondary flex-1" @click="$emit('cancel')">取消</button>
      <button class="btn-primary flex-1" @click="save">儲存</button>
    </div>
  </div>
</template>
