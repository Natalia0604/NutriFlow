import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { WeightRecord } from '@/types'

export const useWeightStore = defineStore('weight', () => {
  const records = ref<WeightRecord[]>([])

  function setRecords(r: WeightRecord[]) { records.value = r }
  function addRecord(r: WeightRecord) { records.value.unshift(r) }
  function removeRecord(id: number) { records.value = records.value.filter(r => r.id !== id) }

  return { records, setRecords, addRecord, removeRecord }
})
