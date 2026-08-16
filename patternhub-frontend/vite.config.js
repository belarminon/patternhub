import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

const backendTarget = process.env.BACKEND_URL || 'https://backend:8080'

export default defineConfig({
  plugins: [react()],
  server: {
    host: true,
    port: 5173,
    proxy: {
      '/api': {
        target: backendTarget,
        changeOrigin: true,
        secure: false,
      },
    },
  },
})
