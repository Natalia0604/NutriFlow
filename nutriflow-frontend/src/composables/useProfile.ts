import { useQuery, useMutation, useQueryClient } from '@tanstack/vue-query'
import { userService } from '@/services/user'
import { useUserStore } from '@/stores/user'

export function useProfile() {
  const userStore = useUserStore()
  const qc = useQueryClient()

  const query = useQuery({
    queryKey: ['profile'],
    queryFn: async () => {
      const res = await userService.getProfile()
      userStore.setProfile(res.data.data!)
      return res.data.data!
    }
  })

  const update = useMutation({
    mutationFn: (data: any) => userService.updateProfile(data),
    onSuccess: () => qc.invalidateQueries({ queryKey: ['profile'] })
  })

  return { ...query, update }
}
