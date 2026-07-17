import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
    // Redirige las llamadas a /api hacia el backend Spring Boot en desarrollo.
    proxy: {
      '/api': 'http://localhost:8080',
    },
  },
})
