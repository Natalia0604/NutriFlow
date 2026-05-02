<script setup lang="ts">
import { ref } from 'vue'
import { useWeight } from '@/composables/useWeight'
import { useProfile } from '@/composables/useProfile'
import AppHeader from '@/components/base/AppHeader.vue'
import WeightLineChart from '@/components/charts/WeightLineChart.vue'
import LoadingSpinner from '@/components/base/LoadingSpinner.vue'
import EmptyState from '@/components/base/EmptyState.vue'
import { useToast } from '@/composables/useToast'
import { today } from '@/utils/date'

const toast = useToast()
const { data: records, isLoading, logWeight } = useWeight(30)
const { data: profile } = useProfile()

const weight = ref('')
const recordDate = ref(today())
const note = ref('')
const saving = ref(false)

async function save() {
  if (!weight.value) return
  saving.value = true
  try {
    await logWeight.mutateAsync({ weight: parseFloat(weight.value), recordDate: recordDate.value, note: note.value || undefined })
    weight.value = ''
    note.value = ''
    toast.success('體重已記錄')
  } catch {
    toast.error('記錄失敗')
  } finally {
    saving.value = false
  }
}
</script>

<template>
  <div>
    <AppHeader title="體重追蹤" />
    <div class="p-4 space-y-4">
      <div class="card space-y-3">
        <h3 class="font-semibold text-gray-700">記錄今日體重</h3>
        <div class="grid grid-cols-2 gap-3">
          <div>
            <label class="block text-xs font-medium text-gray-600 mb-1">體重 (kg)</label>
            <input v-model="weight" type="number" step="0.1" class="input" placeholder="e.g. 62.5" />
          </div>
          <div>
            <label class="block text-xs font-medium text-gray-600 mb-1">日期</label>
            <input v-model="recordDate" type="date" class="input" />
          </div>
        </div>
        <input v-model="note" class="input" placeholder="備注（選填）" />
        <button class="btn-primary w-full" :disabled="!weight || saving" @click="save">
          {{ saving ? '記錄中...' : '記錄' }}
        </button>
      </div>

      <LoadingSpinner v-if="isLoading" />
      <template v-else-if="records?.length">
        <WeightLineChart :records="records" :target-weight="profile?.targetWeight" />

        <div class="card">
          <h3 class="font-semibold text-gray-700 mb-3">近期記錄</h3>
          <div class="space-y-2">
            <div
              v-for="r in records.slice(0, 10)"
              :key="r.id"
              class="flex justify-between items-center text-sm"
            >
              <span class="text-gray-500">{{ r.recordDate }}</span>
              <span class="font-bold text-gray-800">{{ r.weight }} kg</span>
            </div>
          </div>
        </div>
      </template>
      <EmptyState v-else icon="⚖️" title="尚無體重記錄" description="記錄第一筆體重，開始追蹤趨勢" />
    </div>
  </div>
</template>
