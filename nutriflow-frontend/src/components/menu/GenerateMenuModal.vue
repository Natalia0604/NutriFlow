<script setup lang="ts">
import { ref } from 'vue'
import LoadingSpinner from '@/components/base/LoadingSpinner.vue'

const props = defineProps<{ weekStart: string; loading?: boolean }>()
const emit = defineEmits<{ generate: [{ weekStart: string; extraNote: string; forceRegenerate: boolean }]; cancel: [] }>()

const extraNote = ref('')
const forceRegenerate = ref(false)
</script>

<template>
  <Teleport to="body">
    <div class="fixed inset-0 bg-black/40 z-50 flex items-end justify-center p-4" @click.self="$emit('cancel')">
      <div class="bg-white rounded-2xl w-full max-w-sm p-6 space-y-4">
        <h3 class="font-bold text-gray-800 text-lg">✨ AI 生成本週菜單</h3>

        <textarea
          v-model="extraNote"
          class="input"
          rows="3"
          placeholder="額外說明，例如：本週想多吃蔬菜，避免辣食..."
        />

        <label class="flex items-center gap-2 text-sm text-gray-700">
          <input v-model="forceRegenerate" type="checkbox" class="rounded" />
          重新生成（覆蓋現有菜單）
        </label>

        <div class="flex gap-3">
          <button class="btn-secondary flex-1" :disabled="loading" @click="$emit('cancel')">取消</button>
          <button
            class="btn-primary flex-1 flex items-center justify-center gap-2"
            :disabled="loading"
            @click="$emit('generate', { weekStart, extraNote, forceRegenerate })"
          >
            <LoadingSpinner v-if="loading" size="sm" />
            <span>{{ loading ? '生成中...' : '生成菜單' }}</span>
          </button>
        </div>
      </div>
    </div>
  </Teleport>
</template>
