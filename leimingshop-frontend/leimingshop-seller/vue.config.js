/**
 * 配置参考: https://cli.vuejs.org/zh/config/
 */
module.exports = {
  baseUrl: process.env.NODE_ENV === 'production' ? './' : '/',
  productionSourceMap: false,
  devServer: {
    open: true,
    port: 8001,
    overlay: {
      errors: true,
      warnings: true
    }
  }
}
