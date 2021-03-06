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
                proxy: true, // ??????????????????
                prefix: '/web/api', // ???????????????url???????????? /api
                credentials: true // ?????????????????????????????????????????????
        },

        proxy: {
                '/web/api': {
                        target: 'http://leimingshop', // ??????????????????
                        pathRewrite: {
                                '^/web/api': '/', // ??? /api ????????? /
                                changeOrigin: true // ??????????????????
                        }
                }
        },
        // Build Configuration (https://go.nuxtjs.dev/config-build)
        build: {
                extend(config, ctx) {},
                vendor: ['axios'] // ?????????????????????
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
