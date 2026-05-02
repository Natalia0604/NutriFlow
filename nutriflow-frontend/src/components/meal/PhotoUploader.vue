<script setup lang="ts">
import { ref } from 'vue'
import { usePhotoAnalysis } from '@/composables/usePhotoAnalysis'
import LoadingSpinner from '@/components/base/LoadingSpinner.vue'

const emit = defineEmits<{ analyzed: [any[]] }>()

const { analyzing, result, error, analyzeFile } = usePhotoAnalysis()
const fileInput = ref<HTMLInputElement | null>(null)
const preview = ref<string | null>(null)
const storeName = ref('')

async function onFileChange(e: Event) {
  const file = (e.target as HTMLInputElement).files?.[0]
  if (!file) return
  preview.value = URL.createObjectURL(file)
  await analyzeFile(file, storeName.value || undefined)
  if (result.value?.items) {
    emit('analyzed', result.value.items)
  }
}
</script>

<template>
  <div class="space-y-3">
    <input
      v-model="storeName"
      class="input"
      placeholder="店家名稱（選填，提升辨識準確度）"
    />

    <div
      class="border-2 border-dashed border-orange-200 rounded-2xl p-6 text-center cursor-pointer hover:border-primary-400 transition-colors"
      @click="fileInput?.click()"
    >
      <div v-if="analyzing" class="py-4">
        <LoadingSpinner size="md" />
        <p class="text-sm text-gray-500 mt-2">AI 正在辨識食物中...</p>
      </div>
      <div v-else-if="preview">
        <img :src="preview" class="max-h-40 mx-auto rounded-xl object-cover" />
        <p class="text-xs text-gray-400 mt-2">點擊重新上傳</p>
      </div>
      <div v-else>
        <p class="text-4xl mb-2">📷</p>
        <p class="text-sm text-gray-500">點擊上傳餐點照片</p>
        <p class="text-xs text-gray-400">AI 自動辨識食物熱量</p>
      </div>
    </div>

    <p v-if="error" class="text-red-500 text-sm">{{ error }}</p>

    <input ref="fileInput" type="file" accept="image/*" class="hidden" @change="onFileChange" />
  </div>
</template>
