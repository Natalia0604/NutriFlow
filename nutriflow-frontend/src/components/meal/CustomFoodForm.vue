<script setup lang="ts">
import { ref, watch } from 'vue'
import type { CustomFood, SaveCustomFoodRequest } from '@/types'

const props = defineProps<{
  initial?: CustomFood | null
}>()
const emit = defineEmits<{
  save: [SaveCustomFoodRequest]
  cancel: []
}>()

const foodName  = ref(props.initial?.foodName  ?? '')
const sizeLabel = ref(props.initial?.sizeLabel ?? '')
const kcal      = ref<number | ''>(props.initial?.kcal      ?? '')
const protein   = ref<number | ''>(props.initial?.protein   ?? '')
const carbs     = ref<number | ''>(props.initial?.carbs     ?? '')
const fat       = ref<number | ''>(props.initial?.fat       ?? '')
const sodium    = ref<number | ''>(props.initial?.sodium    ?? '')
const note      = ref(props.initial?.note ?? '')

watch(() => props.initial, (v) => {
  foodName.value  = v?.foodName  ?? ''
  sizeLabel.value = v?.sizeLabel ?? ''
  kcal.value      = v?.kcal      ?? ''
  protein.value   = v?.protein   ?? ''
  carbs.value     = v?.carbs     ?? ''
  fat.value       = v?.fat       ?? ''
  sodium.value    = v?.sodium    ?? ''
  note.value      = v?.note      ?? ''
})

function submit() {
  if (!foodName.value || kcal.value === '') return
  emit('save', {
    foodName:  foodName.value,
    sizeLabel: sizeLabel.value || undefined,
    kcal:      Number(kcal.value),
    protein:   protein.value !== '' ? Number(protein.value) : undefined,
    carbs:     carbs.value   !== '' ? Number(carbs.value)   : undefined,
    fat:       fat.value     !== '' ? Number(fat.value)     : undefined,
    sodium:    sodium.value  !== '' ? Number(sodium.value)  : undefined,
    note:      note.value    || undefined,
  })
}
</script>

<template>
  <div class="space-y-3">
    <div>
      <label class="block text-[12px] font-black text-gray-500 mb-1">食品名稱 *</label>
      <input v-model="foodName" class="input text-sm" placeholder="例：自製雞胸肉便當" />
    </div>
    <div class="grid grid-cols-2 gap-2">
      <div>
        <label class="block text-[12px] font-black text-gray-500 mb-1">份量說明</label>
        <input v-model="sizeLabel" class="input text-sm" placeholder="例：1份 / 100g" />
      </div>
      <div>
        <label class="block text-[12px] font-black text-gray-500 mb-1">熱量 (kcal) *</label>
        <input v-model.number="kcal" type="number" min="0" class="input text-sm" placeholder="450" />
      </div>
    </div>
    <div class="grid grid-cols-3 gap-2">
      <div>
        <label class="block text-[11px] font-black text-gray-400 mb-1">蛋白質 (g)</label>
        <input v-model.number="protein" type="number" min="0" class="input text-sm py-2" placeholder="30" />
      </div>
      <div>
        <label class="block text-[11px] font-black text-gray-400 mb-1">碳水 (g)</label>
        <input v-model.number="carbs" type="number" min="0" class="input text-sm py-2" placeholder="45" />
      </div>
      <div>
        <label class="block text-[11px] font-black text-gray-400 mb-1">脂肪 (g)</label>
        <input v-model.number="fat" type="number" min="0" class="input text-sm py-2" placeholder="12" />
      </div>
    </div>
    <div class="grid grid-cols-2 gap-2">
      <div>
        <label class="block text-[11px] font-black text-gray-400 mb-1">鈉 (mg)</label>
        <input v-model.number="sodium" type="number" min="0" class="input text-sm py-2" placeholder="500" />
      </div>
      <div>
        <label class="block text-[11px] font-black text-gray-400 mb-1">備注</label>
        <input v-model="note" class="input text-sm py-2" placeholder="選填" />
      </div>
    </div>
    <div class="flex gap-2 pt-1">
      <button class="btn-secondary flex-1 py-2.5 text-sm" @click="$emit('cancel')">取消</button>
      <button
        class="btn-primary flex-1 py-2.5 text-sm"
        :disabled="!foodName || kcal === ''"
        @click="submit"
      >
        {{ initial ? '更新食品' : '儲存食品' }}
      </button>
    </div>
  </div>
</template>
