<template>
    <div id="app" v-wechat-title="RouteMeta">
        <Header
            class="header"
            v-if="excludeRoutes.indexOf($route.name) == -1 && isRouterAlive"
        ></Header>

        <div
            class="main-content"
            v-if="excludeRoutes.indexOf($route.name) == -1"
        >
            <!-- 部分路由不应该包含这个Footer -->
            <Nuxt v-if="isRouterAlive"></Nuxt>
        </div>
        <Nuxt
            v-if="isRouterAlive && !(excludeRoutes.indexOf($route.name) == -1)"
        ></Nuxt>

        <Footer
            class="footer"
            v-if="excludeRoutes.indexOf($route.name) == -1"
        ></Footer>
    </div>
</template>

<script>
    import Header from "@/components/common/Header";
    import Footer from "@/components/common/Footer";
    import loginPopup from "@/components/common/login";
    import { mapState, mapActions, mapGetters } from "vuex"; //1.引用mapActions辅助函数，用于把全局的actions引用到局部
    export default {
        name: "App",
        head: {
            script: [
                {
                    src: "http://pv.sohu.com/cityjson?ie=utf-8",
                },
            ],
        },
        provide() {
            return {
                reload: this.reload,
            };
        },
        data() {
            return {
                excludeRoutes: [
                    "Login",
                    "ErrorPage",
                    "MyAddress",
                    "AddAddress",
                    "MyOrder",
                    "MyShoppingCart",
                    "register",
                    "successRegister",
                    "forgotPassword",
                    "revise",
                    "treaty",
                    "problemFeedbackIndex",
                ],
                // RouteMeta: this.$route.meta.title,
                RouteMeta: "雷铭B2B2C多用户商城系统演示站",
                isRouterAlive: true,
                needPaddingTop: true,
            };
        },
        computed: {
            ...mapGetters("main", ["getIndexSiteData"]),
            ...mapState("goodsDetails", ["goodsDetails"]),
        },
        components: {
            Header,
            Footer,
            loginPopup,
        },
        created() {
            console.log();
            this.actIndexSite();
        },
        methods: {
            ...mapActions("main", ["actIndexSite"]),

            reload() {
                this.isRouterAlive = false;
                this.$nextTick(() => {
                    this.isRouterAlive = true;
                });
            },
        },
        watch: {
            $route(to, from, next) {
                if (to.name == "HomeIndex") {
                    this.RouteMeta = sessionStorage.getItem("WebsiteName");
                } else if (to.name == "goodsDetails") {
                    return;
                } else {
                    let metaTitle = this.$route.meta.title
                        ? this.$route.meta.title + "-"
                        : "";
                    this.RouteMeta =
                        metaTitle + sessionStorage.getItem("WebsiteName");
                }
            },
            getIndexSiteData(val, oldval) {
                if (this.$route.name == "HomeIndex") {
                    this.RouteMeta = val.name;
                } else if (this.$route.name == "goodsDetails") {
                    return;
                } else {
                    let metaTitle = this.$route.meta.title
                        ? this.$route.meta.title + "-"
                        : "";
                    this.RouteMeta = metaTitle + val.name;
                }
            },

            goodsDetails: {
                immediate: false,
                async handler(newVal, oldval) {
                    if (!sessionStorage.getItem("WebsiteName")) return;

                    await this.$nextTick();

                    if (newVal && newVal.specName) {
                        this.RouteMeta =
                            newVal.specName +
                            "-" +
                            sessionStorage.getItem("WebsiteName");
                    } else {
                        this.RouteMeta = this.getIndexSiteData.name;
                    }
                },
            },
        },
    };
</script>
<style lang="less">
    @import "../themes/red.less";
    @import "../static/css/minireset.css";
</style>

<style lang="less">
    #app {
        height: 100%;
        position: relative;
    }

    .header {
        margin-bottom: -40px;
    }

    .main-content {
        width: 100%;
        // min-height: 100%;
        padding: 40px 0 100px 0;
    }

    .footer {
        margin-top: -15px;
    }

    /*
        * 加载中样式
          */
    .custom-loading {
        transform: scale(1.4);
        letter-spacing: 2px;
    }

    .ivu-icon-ivu-spin-dot {
        display: inline-block !important;
        animation: ani-spin-bounce 1s 0s ease-in-out infinite;
    }

    /*
                                                                                        * 星级
                                                                                    */

    .ivu-rate {
        margin-top: -12px;
    }

    .rate-icon {
        width: 14px;
        height: 14px;
        background: url("/img/03.goodsClass/03-03.goodsDetail/collectAll.png") top
            left/cover;
    }

    .ivu-rate-star-full {
        .rate-icon {
            background: url("/img/03.goodsClass/03-03.goodsDetail/collected.png")
                top left/cover;
        }
    }
</style>
