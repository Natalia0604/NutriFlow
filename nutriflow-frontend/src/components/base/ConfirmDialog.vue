<script setup lang="ts">
defineProps<{ title: string; message: string; confirmLabel?: string; cancelLabel?: string }>()
defineEmits<{ confirm: []; cancel: [] }>()
</script>

<template>
  <Teleport to="body">
    <div class="fixed inset-0 bg-black/40 z-50 flex items-end justify-center p-4" @click.self="$emit('cancel')">
      <div class="bg-white rounded-2xl w-full max-w-sm p-6 space-y-4">
        <h3 class="text-lg font-bold text-gray-800">{{ title }}</h3>
        <p class="text-gray-500 text-sm">{{ message }}</p>
        <div class="flex gap-3">
          <button class="btn-secondary flex-1" @click="$emit('cancel')">
            {{ cancelLabel ?? '取消' }}
          </button>
          <button class="btn-primary flex-1 bg-red-500 hover:bg-red-600" @click="$emit('confirm')">
            {{ confirmLabel ?? '確認' }}
          </button>
        </div>
      </div>
    </div>
  </Teleport>
</template>
