const { defineConfig } = require('@vue/cli-service')

module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    proxy: {
      '^/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        ws: false,
        pathRewrite: { '^/api': '' }
      }
    },
    client: {
      webSocketURL: 'ws://localhost:8081/ws'
    }
  },
  // --- 新增 SCSS 全域變數注入配置 ---
  css: {
    loaderOptions: {
      scss: {
        // 如果是 v8-v10，請改為 prependData
        additionalData: `@import "../vue_mall/src/assets/all.scss";`
      }
    }
  }
})