import { ref } from 'vue'
import { aiService } from '@/services/ai'

export function usePhotoAnalysis() {
  const analyzing = ref(false)
  const result = ref<any | null>(null)
  const error = ref<string | null>(null)

  async function analyzeFile(file: File, storeName?: string) {
    analyzing.value = true
    error.value = null
    result.value = null
    try {
      const base64 = await fileToBase64(file)
      const res = await aiService.analyzePhoto({
        imageBase64: base64,
        mediaType: file.type || 'image/jpeg',
        storeName
      })
      result.value = res.data.data
    } catch (e: any) {
      error.value = e?.response?.data?.error?.message ?? '照片分析失敗'
    } finally {
      analyzing.value = false
    }
  }

  function clear() { result.value = null; error.value = null }

  return { analyzing, result, error, analyzeFile, clear }
}

function fileToBase64(file: File): Promise<string> {
  return new Promise((resolve, reject) => {
    const reader = new FileReader()
    reader.onload = () => resolve((reader.result as string).split(',')[1])
    reader.onerror = reject
    reader.readAsDataURL(file)
  })
}
