import { useQuery, useMutation, useQueryClient } from '@tanstack/vue-query'
import { foodService } from '@/services/food'
import { useToast } from '@/composables/useToast'
import type { SaveCustomFoodRequest } from '@/types'

export function useCustomFood() {
  const qc = useQueryClient()
  const toast = useToast()

  const query = useQuery({
    queryKey: ['customFoods'],
    queryFn: () => foodService.listCustomFoods().then(r => r.data.data ?? [])
  })

  const create = useMutation({
    mutationFn: (data: SaveCustomFoodRequest) =>
      foodService.createCustomFood(data).then(r => r.data.data!),
    onSuccess: () => {
      qc.invalidateQueries({ queryKey: ['customFoods'] })
      toast.success('食品已儲存')
    },
    onError: (err: any) => {
      const msg = err?.response?.data?.error?.message ?? '儲存失敗，請稍後再試'
      toast.error(msg)
    }
  })

  const update = useMutation({
    mutationFn: ({ id, data }: { id: number; data: SaveCustomFoodRequest }) =>
      foodService.updateCustomFood(id, data).then(r => r.data.data!),
    onSuccess: () => {
      qc.invalidateQueries({ queryKey: ['customFoods'] })
      toast.success('食品已更新')
    },
    onError: (err: any) => {
      const msg = err?.response?.data?.error?.message ?? '更新失敗，請稍後再試'
      toast.error(msg)
    }
  })

  const remove = useMutation({
    mutationFn: (id: number) => foodService.deleteCustomFood(id),
    onSuccess: () => {
      qc.invalidateQueries({ queryKey: ['customFoods'] })
      toast.success('已刪除')
    },
    onError: () => toast.error('刪除失敗，請稍後再試')
  })

  return { ...query, create, update, remove }
}
