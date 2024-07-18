import vue from '@vitejs/plugin-vue'
import { defineConfig } from 'vite'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    vue(),
  ],
  resolve: {
    alias: {
      '@': 'src'
    }
  },
  server: {
    port: 5378,
    proxy: {
      '/api': {
        target: 'http://127.0.0.1:11111/douying',
        rewrite: (path) => path.replace(/^\/api/, ""),
        changeOrigin: true
      },
      "/apis":{
        target: 'http://127.0.0.1:1111/ai',
        rewrite: (path) => path.replace(/^\/apis/, ""),
        changeOrigin: true
      }
    }
  },

  rules: [
    {
      test: /\.scss$/,
      use: [
        'style-loader',
        {
          loader: 'css-loader',
          options: { sourceMap: true },
        },
        {
          loader: 'sass-loader',
          options: { sourceMap: true },
        },
      ],
    },
  ],
})
