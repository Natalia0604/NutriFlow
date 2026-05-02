<script setup lang="ts">
import { ref } from 'vue'
import { useCatProfile } from '@/composables/useCatProfile'
import AppHeader from '@/components/base/AppHeader.vue'
import CatProfileCard from '@/components/cat/CatProfileCard.vue'
import CatEditModal from '@/components/cat/CatEditModal.vue'
import CatCalibrationForm from '@/components/cat/CatCalibrationForm.vue'
import LoadingSpinner from '@/components/base/LoadingSpinner.vue'
import EmptyState from '@/components/base/EmptyState.vue'
import { useToast } from '@/composables/useToast'

const toast = useToast()
const { data: cat, isLoading, update } = useCatProfile()
const showEdit = ref(false)
const showCalibration = ref(false)

async function onSave(data: any) {
  try {
    await update.mutateAsync(data)
    showEdit.value = false
    toast.success('貓咪資料已更新')
  } catch {
    toast.error('更新失敗')
  }
}

async function onCalibrate(data: any) {
  try {
    await update.mutateAsync(data)
    showCalibration.value = false
    toast.success('校正完成')
  } catch {
    toast.error('校正失敗')
  }
}
</script>

<template>
  <div>
    <AppHeader title="貓咪設定" :back="true" />
    <div class="p-4 space-y-4">
      <LoadingSpinner v-if="isLoading" />
      <template v-else-if="cat">
        <CatProfileCard :cat="cat" @edit="showEdit = true" />
        <button class="btn-secondary w-full" @click="showCalibration = !showCalibration">
          📏 碗口校正
        </button>
        <CatCalibrationForm v-if="showCalibration" @save="onCalibrate" @cancel="showCalibration = false" />
      </template>
      <EmptyState v-else icon="🐱" title="尚未設定貓咪資料">
        <button class="btn-primary mt-4" @click="showEdit = true">新增貓咪</button>
      </EmptyState>

      <CatEditModal v-if="showEdit" :cat="cat ?? { id: 0, breed: '', name: '' }" @save="onSave" @cancel="showEdit = false" />
    </div>
  </div>
</template>
