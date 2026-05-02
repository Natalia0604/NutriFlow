import { useQuery, useMutation, useQueryClient } from '@tanstack/vue-query'
import { menuService } from '@/services/menu'
import { computed, type Ref } from 'vue'

export function useMenu(weekStart: Ref<string>) {
  const qc = useQueryClient()

  const query = useQuery({
    queryKey: computed(() => ['menu', weekStart.value]),
    queryFn: () => menuService.getWeekMenu(weekStart.value).then(r => r.data.data!),
    enabled: computed(() => !!weekStart.value)
  })

  const generate = useMutation({
    mutationFn: (data: { weekStart: string; extraNote?: string; forceRegenerate?: boolean }) =>
      menuService.generateMenu(data).then(r => r.data.data!),
    onSuccess: () => qc.invalidateQueries({ queryKey: ['menu'] })
  })

  const createBlank = useMutation({
    mutationFn: (data: { weekStart: string }) =>
      menuService.createBlankMenu(data).then(r => r.data.data!),
    onSuccess: () => qc.invalidateQueries({ queryKey: ['menu'] })
  })

  const updateCell = useMutation({
    mutationFn: ({ menuId, data }: { menuId: number; data: any }) =>
      menuService.updateCell(menuId, data).then(r => r.data.data!),
    onSuccess: () => qc.invalidateQueries({ queryKey: ['menu'] })
  })

  return { ...query, generate, createBlank, updateCell }
}
