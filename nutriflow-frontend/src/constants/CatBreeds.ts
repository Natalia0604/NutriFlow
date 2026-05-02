export type CatBreed = 'orange' | 'calico' | 'black' | 'white' | 'tabby' | 'ragdoll'

export interface CatBreedInfo {
  value: CatBreed
  label: string
  tag: string
  icon: string
  svg: string
}

export const CAT_BREEDS: CatBreedInfo[] = [
  {
    value: 'orange',
    label: '橘貓小橘',
    tag: '活潑・愛吃',
    icon: '🧡',
    svg: `
      <ellipse cx="60" cy="88" rx="32" ry="26" fill="#FFBB66"/>
      <ellipse cx="60" cy="88" rx="18" ry="15" fill="#FFE0A8"/>
      <ellipse cx="60" cy="54" rx="27" ry="26" fill="#FFBB66"/>
      <path d="M42,50 Q55,46 68,50" stroke="#E8943A" stroke-width="1.5" fill="none" opacity=".5"/>
      <path d="M40,56 Q55,52 70,56" stroke="#E8943A" stroke-width="1.5" fill="none" opacity=".5"/>
      <polygon points="40,36 35,18 50,32" fill="#FFBB66"/>
      <polygon points="80,36 85,18 70,32" fill="#FFBB66"/>
      <polygon points="41,34 37,22 49,31" fill="#FFB3C6"/>
      <polygon points="79,34 83,22 71,31" fill="#FFB3C6"/>
      <path d="M50,52 Q56,47 62,52" fill="none" stroke="#3D2B1F" stroke-width="2.5" stroke-linecap="round"/>
      <path d="M64,52 Q70,47 76,52" fill="none" stroke="#3D2B1F" stroke-width="2.5" stroke-linecap="round"/>
      <ellipse cx="46" cy="60" rx="6" ry="4" fill="#FFB3C6" opacity=".5"/>
      <ellipse cx="74" cy="60" rx="6" ry="4" fill="#FFB3C6" opacity=".5"/>
      <ellipse cx="60" cy="62" rx="3" ry="2" fill="#FF85A1"/>
      <path d="M57,65 Q60,69 63,65" fill="none" stroke="#3D2B1F" stroke-width="1.5" stroke-linecap="round"/>
      <line x1="35" y1="60" x2="53" y2="62" stroke="#3D2B1F" stroke-width="1" opacity=".5"/>
      <line x1="85" y1="60" x2="67" y2="62" stroke="#3D2B1F" stroke-width="1" opacity=".5"/>
      <path d="M88,100 Q100,88 97,74" stroke="#FFBB66" stroke-width="8" fill="none" stroke-linecap="round"/>
    `
  },
  {
    value: 'calico',
    label: '三花小花',
    tag: '溫柔・黏人',
    icon: '🌸',
    svg: `
      <ellipse cx="60" cy="88" rx="32" ry="26" fill="#F5F5F0"/>
      <ellipse cx="60" cy="88" rx="18" ry="15" fill="#FFF8F4"/>
      <ellipse cx="60" cy="54" rx="27" ry="26" fill="#F5F5F0"/>
      <ellipse cx="46" cy="50" rx="10" ry="8" fill="#FFBB66" opacity=".85"/>
      <ellipse cx="72" cy="60" rx="9" ry="7" fill="#2C2C2C" opacity=".75"/>
      <ellipse cx="55" cy="82" rx="8" ry="6" fill="#FFBB66" opacity=".7"/>
      <polygon points="40,36 35,18 50,32" fill="#F5F5F0"/>
      <polygon points="80,36 85,18 70,32" fill="#F5F5F0"/>
      <polygon points="41,34 37,22 49,31" fill="#FFB3C6"/>
      <polygon points="79,34 83,22 71,31" fill="#FFB3C6"/>
      <path d="M50,52 Q56,47 62,52" fill="none" stroke="#3D2B1F" stroke-width="2.5" stroke-linecap="round"/>
      <path d="M64,52 Q70,47 76,52" fill="none" stroke="#3D2B1F" stroke-width="2.5" stroke-linecap="round"/>
      <ellipse cx="46" cy="60" rx="6" ry="4" fill="#FFB3C6" opacity=".5"/>
      <ellipse cx="74" cy="60" rx="6" ry="4" fill="#FFB3C6" opacity=".5"/>
      <ellipse cx="60" cy="62" rx="3" ry="2" fill="#FF85A1"/>
      <path d="M57,65 Q60,69 63,65" fill="none" stroke="#3D2B1F" stroke-width="1.5" stroke-linecap="round"/>
      <line x1="35" y1="60" x2="53" y2="62" stroke="#3D2B1F" stroke-width="1" opacity=".5"/>
      <line x1="85" y1="60" x2="67" y2="62" stroke="#3D2B1F" stroke-width="1" opacity=".5"/>
      <path d="M88,100 Q100,88 97,74" stroke="#F5F5F0" stroke-width="8" fill="none" stroke-linecap="round"/>
    `
  },
  {
    value: 'black',
    label: '黑貓小墨',
    tag: '神秘・聰明',
    icon: '🖤',
    svg: `
      <ellipse cx="60" cy="88" rx="32" ry="26" fill="#2C2C2C"/>
      <ellipse cx="60" cy="88" rx="18" ry="15" fill="#3D3D3D"/>
      <ellipse cx="60" cy="54" rx="27" ry="26" fill="#2C2C2C"/>
      <polygon points="40,36 35,18 50,32" fill="#2C2C2C"/>
      <polygon points="80,36 85,18 70,32" fill="#2C2C2C"/>
      <polygon points="41,34 37,22 49,31" fill="#7B4F6E"/>
      <polygon points="79,34 83,22 71,31" fill="#7B4F6E"/>
      <ellipse cx="52" cy="52" rx="6" ry="7" fill="#7FD18A"/>
      <ellipse cx="68" cy="52" rx="6" ry="7" fill="#7FD18A"/>
      <ellipse cx="52" cy="53" rx="3" ry="5" fill="#1A1A1A"/>
      <ellipse cx="68" cy="53" rx="3" ry="5" fill="#1A1A1A"/>
      <circle cx="50" cy="51" r="1.5" fill="white" opacity=".8"/>
      <circle cx="66" cy="51" r="1.5" fill="white" opacity=".8"/>
      <ellipse cx="46" cy="61" rx="6" ry="4" fill="#7B4F6E" opacity=".5"/>
      <ellipse cx="74" cy="61" rx="6" ry="4" fill="#7B4F6E" opacity=".5"/>
      <ellipse cx="60" cy="63" rx="3" ry="2" fill="#C46E9A"/>
      <path d="M57,66 Q60,70 63,66" fill="none" stroke="#888" stroke-width="1.5" stroke-linecap="round"/>
      <line x1="35" y1="61" x2="53" y2="63" stroke="#888" stroke-width="1" opacity=".4"/>
      <line x1="85" y1="61" x2="67" y2="63" stroke="#888" stroke-width="1" opacity=".4"/>
      <path d="M88,100 Q100,88 97,74" stroke="#2C2C2C" stroke-width="8" fill="none" stroke-linecap="round"/>
    `
  },
  {
    value: 'white',
    label: '白貓小雪',
    tag: '優雅・安靜',
    icon: '🤍',
    svg: `
      <ellipse cx="60" cy="88" rx="32" ry="26" fill="#F8F8FF"/>
      <ellipse cx="60" cy="88" rx="18" ry="15" fill="#FFF5F8"/>
      <ellipse cx="60" cy="54" rx="27" ry="26" fill="#F8F8FF"/>
      <polygon points="40,36 35,18 50,32" fill="#F8F8FF"/>
      <polygon points="80,36 85,18 70,32" fill="#F8F8FF"/>
      <polygon points="41,34 37,22 49,31" fill="#FFD6E7"/>
      <polygon points="79,34 83,22 71,31" fill="#FFD6E7"/>
      <ellipse cx="52" cy="52" rx="6" ry="7" fill="#7EC8E3"/>
      <ellipse cx="68" cy="52" rx="6" ry="7" fill="#7EC8E3"/>
      <ellipse cx="52" cy="53" rx="3" ry="5" fill="#1A1A1A"/>
      <ellipse cx="68" cy="53" rx="3" ry="5" fill="#1A1A1A"/>
      <circle cx="50" cy="51" r="1.5" fill="white" opacity=".9"/>
      <circle cx="66" cy="51" r="1.5" fill="white" opacity=".9"/>
      <ellipse cx="46" cy="61" rx="6" ry="4" fill="#FFD6E7" opacity=".6"/>
      <ellipse cx="74" cy="61" rx="6" ry="4" fill="#FFD6E7" opacity=".6"/>
      <ellipse cx="60" cy="63" rx="3" ry="2" fill="#FFB3C6"/>
      <path d="M57,66 Q60,70 63,66" fill="none" stroke="#CCC" stroke-width="1.5" stroke-linecap="round"/>
      <line x1="35" y1="61" x2="53" y2="63" stroke="#CCC" stroke-width="1" opacity=".5"/>
      <line x1="85" y1="61" x2="67" y2="63" stroke="#CCC" stroke-width="1" opacity=".5"/>
      <path d="M88,100 Q100,88 97,74" stroke="#F0F0F8" stroke-width="8" fill="none" stroke-linecap="round"/>
    `
  },
  {
    value: 'tabby',
    label: '虎斑小虎',
    tag: '勇敢・頑皮',
    icon: '🐯',
    svg: `
      <ellipse cx="60" cy="88" rx="32" ry="26" fill="#C8A882"/>
      <ellipse cx="60" cy="88" rx="18" ry="15" fill="#E8D4B8"/>
      <ellipse cx="60" cy="54" rx="27" ry="26" fill="#C8A882"/>
      <path d="M38,48 Q48,43 58,48" stroke="#8B6914" stroke-width="2" fill="none" opacity=".6"/>
      <path d="M62,48 Q72,43 82,48" stroke="#8B6914" stroke-width="2" fill="none" opacity=".6"/>
      <path d="M36,55 Q50,50 64,55" stroke="#8B6914" stroke-width="2" fill="none" opacity=".5"/>
      <path d="M40,88 Q55,82 70,88" stroke="#A0824A" stroke-width="2" fill="none" opacity=".4"/>
      <path d="M50,35 Q55,30 60,35 Q65,30 70,35" stroke="#8B6914" stroke-width="2" fill="none" opacity=".6"/>
      <polygon points="40,36 35,18 50,32" fill="#C8A882"/>
      <polygon points="80,36 85,18 70,32" fill="#C8A882"/>
      <polygon points="41,34 37,22 49,31" fill="#FFB3C6"/>
      <polygon points="79,34 83,22 71,31" fill="#FFB3C6"/>
      <path d="M50,52 Q56,47 62,52" fill="none" stroke="#3D2B1F" stroke-width="2.5" stroke-linecap="round"/>
      <path d="M64,52 Q70,47 76,52" fill="none" stroke="#3D2B1F" stroke-width="2.5" stroke-linecap="round"/>
      <ellipse cx="46" cy="60" rx="6" ry="4" fill="#FFB3C6" opacity=".5"/>
      <ellipse cx="74" cy="60" rx="6" ry="4" fill="#FFB3C6" opacity=".5"/>
      <ellipse cx="60" cy="62" rx="3" ry="2" fill="#FF85A1"/>
      <path d="M57,65 Q60,69 63,65" fill="none" stroke="#3D2B1F" stroke-width="1.5" stroke-linecap="round"/>
      <line x1="35" y1="60" x2="53" y2="62" stroke="#3D2B1F" stroke-width="1" opacity=".5"/>
      <line x1="85" y1="60" x2="67" y2="62" stroke="#3D2B1F" stroke-width="1" opacity=".5"/>
      <path d="M88,100 Q100,88 97,74" stroke="#C8A882" stroke-width="8" fill="none" stroke-linecap="round"/>
    `
  },
  {
    value: 'ragdoll',
    label: '布偶小棉',
    tag: '溫順・可愛',
    icon: '💜',
    svg: `
      <ellipse cx="60" cy="90" rx="35" ry="28" fill="#F0EAE0"/>
      <ellipse cx="60" cy="88" rx="20" ry="16" fill="#FFF8F0"/>
      <ellipse cx="60" cy="76" rx="22" ry="8" fill="#F8F4EC"/>
      <ellipse cx="60" cy="54" rx="28" ry="27" fill="#F0EAE0"/>
      <ellipse cx="60" cy="56" rx="18" ry="14" fill="#C8B49A" opacity=".35"/>
      <polygon points="40,36 33,14 50,30" fill="#F0EAE0"/>
      <polygon points="80,36 87,14 70,30" fill="#F0EAE0"/>
      <polygon points="41,34 36,19 49,29" fill="#C8A888"/>
      <polygon points="79,34 84,19 71,29" fill="#C8A888"/>
      <ellipse cx="51" cy="51" rx="7" ry="8" fill="#5BB8D4"/>
      <ellipse cx="69" cy="51" rx="7" ry="8" fill="#5BB8D4"/>
      <ellipse cx="51" cy="52" rx="3.5" ry="5.5" fill="#1A1A1A"/>
      <ellipse cx="69" cy="52" rx="3.5" ry="5.5" fill="#1A1A1A"/>
      <circle cx="49" cy="49" r="2" fill="white" opacity=".85"/>
      <circle cx="67" cy="49" r="2" fill="white" opacity=".85"/>
      <ellipse cx="46" cy="62" rx="7" ry="4" fill="#FFD6E7" opacity=".5"/>
      <ellipse cx="74" cy="62" rx="7" ry="4" fill="#FFD6E7" opacity=".5"/>
      <ellipse cx="60" cy="64" rx="3.5" ry="2.5" fill="#D4849A"/>
      <path d="M56,67 Q60,72 64,67" fill="none" stroke="#999" stroke-width="1.5" stroke-linecap="round"/>
      <line x1="33" y1="62" x2="52" y2="64" stroke="#CCC" stroke-width="1" opacity=".6"/>
      <line x1="87" y1="62" x2="68" y2="64" stroke="#CCC" stroke-width="1" opacity=".6"/>
      <path d="M90,102 Q104,90 100,74" stroke="#E8E0D4" stroke-width="10" fill="none" stroke-linecap="round"/>
    `
  }
]

export const CAT_BREED_ICONS: Record<CatBreed, string> = {
  orange: '🧡',
  calico: '🌸',
  black: '🖤',
  white: '🤍',
  tabby: '🐯',
  ragdoll: '💜',
}

export const CAT_BREED_NAMES: Record<CatBreed, string> = {
  orange: '小橘',
  calico: '小花',
  black: '小墨',
  white: '小雪',
  tabby: '小虎',
  ragdoll: '小棉',
}
