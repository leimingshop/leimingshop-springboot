const env = require('./env')
export default {
        // Global page headers (https://go.nuxtjs.dev/config-head)
        head: {
                title: 'nuxt-app',
                meta: [{
                                charset: 'utf-8'
                        },
                        {
                                name: 'viewport',
                                content: 'width=device-width, initial-scale=1'
                        },
                        {
                                hid: 'description',
                                name: 'description',
                                content: ''
                        }
                ],
                link: [{
                        rel: 'icon',
                        type: 'image/x-icon',
                        href: '/favicon.ico'
                }]
        },

        // Global CSS (https://go.nuxtjs.dev/config-css)
        css: [
                // 'iview/dist/styles/iview.css',
                './static/css/swiper.css',
                // './static/css/steps.less'
                // {src:'view-design/src/styles/index.less',javascriptEnabled: true}
        ],

        // Plugins to run before rendering page (https://go.nuxtjs.dev/config-plugins)
        plugins: [
                '@/plugins/iview',
                {
                        src: "~/plugins/swiper.js",
                        ssr: false
                },
                '@/plugins/viewer.js',
                {
                        src: "~/plugins/vueScroll.js",
                        ssr: false
                },
                '@/plugins/viewDesign.js',
                '@/plugins/router.js'
        ],

        // Auto import components (https://go.nuxtjs.dev/config-components)
        components: true,

        // Modules for dev and build (recommended) (https://go.nuxtjs.dev/config-modules)
        buildModules: [],

        // Modules (https://go.nuxtjs.dev/config-modules)
        modules: [
                '@nuxtjs/axios',

        ],

        axios: {
                proxy: true, // 表示开启代理
                prefix: '/web/api', // 表示给请求url加个前缀 /api
                credentials: true // 表示跨域请求时是否需要使用凭证
        },

        proxy: {
                '/web/api': {
                        target: 'http://leimingshop', // 目标接口域名
                        pathRewrite: {
                                '^/web/api': '/', // 把 /api 替换成 /
                                changeOrigin: true // 表示是否跨域
                        }
                }
        },
        // Build Configuration (https://go.nuxtjs.dev/config-build)
        build: {
                extend(config, ctx) {},
                vendor: ['axios'] // 为防止重复打包
        },
        env: {
                BASE_URL: env[process.env.MODE].ENV_API,
                NODE_ENV: env[process.env.MODE].MODE
        },
        server: {
                port: 8000, // default: 3000
                host: '0.0.0.0' // default: localhost,
        }
}
