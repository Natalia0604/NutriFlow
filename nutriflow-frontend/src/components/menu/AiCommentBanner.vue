<script setup lang="ts">
defineProps<{ comment: string }>()
</script>

<template>
  <div class="bg-gradient-to-r from-amber-50 to-orange-50 border border-amber-200 rounded-2xl p-4 flex items-start gap-3">
    <span class="text-2xl flex-shrink-0">🤖</span>
    <p class="text-sm text-gray-700 leading-relaxed">{{ comment }}</p>
  </div>
</template>
