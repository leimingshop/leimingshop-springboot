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
                contentmleft: "215px",
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
                return this.$http
                    .get("/admin-api/user/info")
                    .then(({ data: res }) => {
                        if (res.code !== 200) {
                            return this.$message.error(res.msg);
                        }
                        this.$store.state.user.id = res.data.id;
                        this.$store.state.user.name = res.data.username;
                        this.$store.state.user.realName = res.data.realName;
                        this.$store.state.user.superadmin = res.data.superadmin;
                    })
                    .catch(() => {});
            },
            // 获取权限
            getPermissions() {
                return this.$http
                    .get("/admin-api/menu/permissions")
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
    }

    .bigStyle {
        margin: 0;
        height: auto;
    }
</style>
