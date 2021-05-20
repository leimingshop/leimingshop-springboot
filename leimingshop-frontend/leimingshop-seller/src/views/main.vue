<template>
    <div
        v-loading.fullscreen.lock="loading"
        :element-loading-text="$t('loading')"
        :class="[
            'aui-wrapper',
            { 'aui-sidebar--fold': $store.state.sidebarFold },
            { 'aui-wrapper-bigSyle': $store.state.mainSwitch },
        ]"
    >
        <template v-if="!loading">
            <main-navbar
                v-show="!$store.state.mainSwitch"
                ref="mainNavbarCompon"
                @setParentItem="setParentItem"
                :parentArr="parentArr"
            />
            <main-sidebar
                v-show="!$store.state.mainSwitch"
                @setParentArr="setParentArr"
                :parentItem="parentItem"
            />
            <div
                class="aui-content__wrapper"
                :class="{ bigStyle: $store.state.mainSwitch }"
            >
                <main-content v-if="!$store.state.contentIsNeedRefresh" />
            </div>
            <!-- <main-theme-tools /> -->
        </template>
    </div>
</template>

<script>
    import MainNavbar from "./main-navbar";
    import MainSidebar from "./main-sidebar";
    import MainContent from "./main-content";
    import MainThemeTools from "./main-theme-tools";
    import debounce from "lodash/debounce";
    export default {
        provide() {
            return {
                // 刷新
                refresh() {
                    this.$store.state.contentIsNeedRefresh = true;
                    this.$nextTick(() => {
                        this.$store.state.contentIsNeedRefresh = false;
                    });
                },
            };
        },
        data() {
            return {
                loading: true,
                parentArr: [],
                parentItem: {
                    id: 0,
                    title: "首页",
                    children: [],
                },
            };
        },
        components: {
            MainNavbar,
            MainSidebar,
            MainContent,
            MainThemeTools,
        },
        watch: {
            $route: "routeHandle",
        },
        created() {
            this.windowResizeHandle();
            this.routeHandle(this.$route);
            Promise.all([this.getUserInfo(), this.getPermissions()]).then(() => {
                this.loading = false;
            });
        },
        methods: {
            setParentItem(item) {
                console.log(item);
                this.parentItem = item;
            },
            setParentArr(arr) {
                this.parentArr = arr;
            },
            // 窗口改变大小
            windowResizeHandle() {
                this.$store.state.sidebarFold =
                    document.documentElement["clientWidth"] <= 992 || false;
                window.addEventListener(
                    "resize",
                    debounce(() => {
                        this.$store.state.sidebarFold =
                            document.documentElement["clientWidth"] <= 992 || false;
                    }, 150)
                );
            },
            // 路由, 监听
            routeHandle(route) {
                if (!route.meta.isTab) {
                    return false;
                }
                var tab = this.$store.state.contentTabs.filter(
                    (item) => item.name === route.name
                )[0];
                if (!tab) {
                    tab = {
                        ...window.SITE_CONFIG["contentTabDefault"],
                        ...route.meta,
                        name: route.name,
                        params: { ...route.params },
                        query: { ...route.query },
                    };
                    this.$store.state.contentTabs = this.$store.state.contentTabs.concat(
                        tab
                    );
                }
                this.$store.state.sidebarMenuActiveName = tab.menuId;
                this.$store.state.contentTabsActiveName = tab.name;
            },
            // 获取当前管理员信息
            getUserInfo() {
                // this.$http.get('/seller-api/setting/find/store').then(({ data: res }) => {
                //     this.dataForm.username = res.data.account;
                //   })
                return this.$http
                    .get("/seller-api/setting/find/store")
                    .then(({ data: res }) => {
                        if (res.code !== 200) {
                            return this.$message.error(res.msg);
                        }
                        const storeId = localStorage.getItem("storeId");
                        if (storeId !== res.data.storeId) {
                            localStorage.setItem("storeId", res.data.storeId);
                            localStorage.removeItem("chooseHistory");
                        }
                        this.$store.state.user.id = res.data.id;
                        this.$store.state.user.storeId = res.data.storeId;
                        this.$store.state.user.name = res.data.account;
                        this.$store.state.user.logo = res.data.logo;
                        this.$store.state.user.qrCode = res.data.qrCode;
                        this.$store.state.user.indexMenu = res.data.indexMenu;
                        //  如果是登录进来的，左侧定位到第一级的第一个菜单
                        window.that = this;
                        if (window.isloaingEntry) {
                            // if(!res.data.indexMenu){
                            //   this.$router.replace({ name: 'home' })
                            // }else{
                            //   this.$router.replace({ name: 'storehome' })
                            // }

                            this.$router.replace({
                                name:
                                    window.SITE_CONFIG["dynamicMenuRoutes"][0].name,
                            });
                            this.setTimeout(() => {
                                window.that.$refs.mainNavbarCompon.refreshAside(
                                    "nav" +
                                        window.SITE_CONFIG[
                                            "dynamicMenuRoutes"
                                        ][0].name.split("-")[0]
                                );
                            }, 300);
                        } else {
                            //  如果是刷新的本来左侧菜单定位在哪，就定位在哪
                            // 如果是直接访问url
                            // alert(window.location.hash);
                            if (
                                window.location.hash == "#/home" ||
                                !window.location.hash == "#/" ||
                                !window.location.hash == "#"
                            ) {
                                this.$router.replace({
                                    name:
                                        window.SITE_CONFIG["dynamicMenuRoutes"][0]
                                            .name,
                                });
                            }
                        }
                    })
                    .catch(() => {});
            },
            // 获取权限
            getPermissions() {
                return this.$http
                    .get("/seller-api/menu/permissions")
                    .then(({ data: res }) => {
                        if (res.code !== 200) {
                            return this.$message.error(res.msg);
                        }
                        window.SITE_CONFIG["permissions"] = res.data;
                    })
                    .catch(() => {});
            },
        },
    };
</script>
<style lang="scss"  scoped>
    .aui-wrapper {
        background: #ecedf1;
        padding-top: 80px;
    }
    /deep/ .el-breadcrumb {
        margin-bottom: 25px;
    }
    /deep/ .aui-content__wrapper {
        margin-right: 10px;
        margin-top: 56px;
        border-top-left-radius: 5px;
        border-top-right-radius: 5px;
        /*border-bottom:20px solid #ECEDF1;*/
        margin-left: 216px;
        background-color: white;
        .aui-content {
            /deep/ .aui-content--tabs-tools {
                top: 80px !important;
                /*right: 20px !important;*/
                height: 45px;
                line-height: 45px;
            }
        }
    }
    /deep/ .el-tabs__nav-prev,
    /deep/ .el-tabs__nav-next {
        line-height: 54px !important;
    }

    .aui-sidebar--fold .aui-content__wrapper {
        margin-left: 75px !important;
    }

    .aui-wrapper-bigSyle {
        padding: 0;
        .aui-content__wrapper {
            margin: 0;
        }
    }

    .bigStyle {
        margin: 0;
        height: auto;
    }
</style>