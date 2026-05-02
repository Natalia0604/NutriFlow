import type { Config } from 'tailwindcss'

export default {
  content: ['./index.html', './src/**/*.{vue,js,ts,jsx,tsx}'],
  theme: {
    extend: {
      colors: {
        primary: {
          50: '#fff7ed',
          100: '#ffedd5',
          200: '#fed7aa',
          300: '#fdba74',
          400: '#fb923c',
          500: '#f97316',
          600: '#ea580c',
          700: '#c2410c',
          800: '#9a3412',
          900: '#7c2d12'
        },
        cat: {
          orange: '#FF8C42',
          cream: '#FFF3E0',
          brown: '#6D4C41'
        }
      },
      fontFamily: {
        sans: ['Inter', 'Noto Sans TC', 'sans-serif']
      },
      borderRadius: {
        'xl': '1rem',
        '2xl': '1.5rem',
        '3xl': '2rem'
      }
    }
  },
  plugins: []
} satisfies Config
