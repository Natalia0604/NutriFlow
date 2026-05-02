import { useQuery, useMutation, useQueryClient } from '@tanstack/vue-query'
import { weightService } from '@/services/weight'

export function useWeight(limit = 30) {
  const qc = useQueryClient()

  const query = useQuery({
    queryKey: ['weights', limit],
    queryFn: () => weightService.getHistory(limit).then(r => r.data.data!)
  })

  const logWeight = useMutation({
    mutationFn: (data: { weight: number; recordDate: string; note?: string }) =>
      weightService.logWeight(data).then(r => r.data.data!),
    onSuccess: () => qc.invalidateQueries({ queryKey: ['weights'] })
  })

  const deleteRecord = useMutation({
    mutationFn: (id: number) => weightService.deleteRecord(id),
    onSuccess: () => qc.invalidateQueries({ queryKey: ['weights'] })
  })

  return { ...query, logWeight, deleteRecord }
}
