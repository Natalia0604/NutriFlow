import { useQuery, useMutation, useQueryClient } from '@tanstack/vue-query'
import { userService } from '@/services/user'

export function useCatProfile() {
  const qc = useQueryClient()

  const query = useQuery({
    queryKey: ['catProfile'],
    queryFn: () => userService.getCat().then(r => r.data.data!)
  })

  const update = useMutation({
    mutationFn: (data: any) => userService.updateCat(data).then(r => r.data.data!),
    onSuccess: () => qc.invalidateQueries({ queryKey: ['catProfile'] })
  })

  return { ...query, update }
}
