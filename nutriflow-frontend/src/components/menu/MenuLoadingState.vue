<script setup lang="ts">
import CatMascot from '@/components/cat/CatMascot.vue'
</script>

<template>
  <div class="flex flex-col items-center justify-center py-20 space-y-4">
    <CatMascot mood="excited" size="lg" :animated="true" />
    <p class="text-gray-600 font-semibold">貓咪正在幫你規劃菜單...</p>
    <p class="text-gray-400 text-sm">AI 分析中，請稍候片刻</p>
  </div>
</template>
