import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import path from 'path'

export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': path.resolve(__dirname, 'src')
    }
  },
  build: {
    outDir: 'target/dist'
  },
  server: {
    port: 8091,
    proxy: {
      '/generate': 'http://localhost:8090',
      '/validate': 'http://localhost:8090',
      '/batch': 'http://localhost:8090'
    }
  }
})
