<script setup lang="ts">
import { RouterView } from 'vue-router'
import BottomNav from '@/components/base/BottomNav.vue'
import ToastContainer from '@/components/base/ToastContainer.vue'
</script>

<template>
  <div class="min-h-screen flex flex-col items-center" style="background:#FFF8F0">
    <!-- Decorative bg blobs -->
    <div class="pointer-events-none fixed inset-0 overflow-hidden z-0">
      <div class="absolute top-[10%] left-[10%] w-64 h-64 rounded-full bg-pink-200/20 blur-3xl" />
      <div class="absolute top-[5%] right-[10%] w-56 h-56 rounded-full bg-purple-200/20 blur-3xl" />
      <div class="absolute bottom-[15%] right-[5%] w-72 h-72 rounded-full bg-emerald-200/20 blur-3xl" />
      <div class="absolute bottom-[10%] left-[5%] w-60 h-60 rounded-full bg-yellow-200/15 blur-3xl" />
    </div>

    <div class="relative z-10 w-full max-w-[420px] min-h-screen flex flex-col pb-[72px]">
      <main class="flex-1 overflow-y-auto">
        <RouterView />
      </main>
    </div>

    <BottomNav />
    <ToastContainer />
  </div>
</template>
