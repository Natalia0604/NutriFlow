<script setup lang="ts">
import { useToast } from '@/composables/useToast'

const { toasts, dismiss } = useToast()
</script>

<template>
  <Teleport to="body">
    <div class="fixed top-4 right-4 z-50 flex flex-col gap-2 pointer-events-none">
      <Transition
        v-for="toast in toasts"
        :key="toast.id"
        enter-active-class="transition-all duration-300"
        enter-from-class="opacity-0 translate-x-4"
        leave-active-class="transition-all duration-200"
        leave-to-class="opacity-0 translate-x-4"
      >
        <div
          class="pointer-events-auto flex items-center gap-2 px-4 py-3 rounded-xl shadow-lg text-white text-sm font-medium max-w-xs"
          :class="{
            'bg-green-500': toast.type === 'success',
            'bg-red-500': toast.type === 'error',
            'bg-gray-700': toast.type === 'info'
          }"
          @click="dismiss(toast.id)"
        >
          <span>{{ toast.message }}</span>
        </div>
      </Transition>
    </div>
  </Teleport>
</template>
