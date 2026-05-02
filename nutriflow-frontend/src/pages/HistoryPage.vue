<script setup lang="ts">
import { ref } from 'vue'
import { useQuery } from '@tanstack/vue-query'
import { mealService } from '@/services/meal'
import AppHeader from '@/components/base/AppHeader.vue'
import MealLogCard from '@/components/meal/MealLogCard.vue'
import LoadingSpinner from '@/components/base/LoadingSpinner.vue'
import EmptyState from '@/components/base/EmptyState.vue'

const page = ref(0)

const { data, isLoading } = useQuery({
  queryKey: ['meals', 'history', page],
  queryFn: () => mealService.getHistory(page.value, 20).then(r => r.data.data!)
})
</script>

<template>
  <div>
    <AppHeader title="飲食紀錄" />
    <div class="p-4 space-y-3">
      <LoadingSpinner v-if="isLoading" size="md" />
      <EmptyState v-else-if="!data?.content?.length" icon="📋" title="尚無飲食紀錄" />
      <template v-else>
        <MealLogCard
          v-for="log in data.content"
          :key="log.id"
          :log="log"
          @delete="() => {}"
        />
        <div class="flex justify-center gap-3 py-2">
          <button
            class="btn-secondary text-sm"
            :disabled="page === 0"
            @click="page = Math.max(0, page - 1)"
          >← 上一頁</button>
          <span class="text-sm text-gray-500 self-center">第 {{ page + 1 }} / {{ data.totalPages }} 頁</span>
          <button
            class="btn-secondary text-sm"
            :disabled="data.last"
            @click="page++"
          >下一頁 →</button>
        </div>
      </template>
    </div>
  </div>
</template>
