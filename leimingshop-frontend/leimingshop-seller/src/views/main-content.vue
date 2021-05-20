<template>
    <main :class="['aui-content', { 'aui-content--tabs': $route.meta.isTab }]">
        <!-- tab展示内容 -->
        <template v-if="$route.meta.isTab">
            <el-dropdown
                v-show="!$store.state.mainSwitch"
                class="aui-content--tabs-tools"
            >
                <i class="el-icon-arrow-down"></i>
                <el-dropdown-menu slot="dropdown" :show-timeout="0">
                    <el-dropdown-item
                        @click.native="
                            tabRemoveHandle($store.state.contentTabsActiveName)
                        "
                        >{{ $t("contentTabs.closeCurrent") }}</el-dropdown-item
                    >
                    <el-dropdown-item @click.native="tabsCloseOtherHandle()">{{
                        $t("contentTabs.closeOther")
                    }}</el-dropdown-item>
                    <el-dropdown-item @click.native="tabsCloseAllHandle()">{{
                        $t("contentTabs.closeAll")
                    }}</el-dropdown-item>
                </el-dropdown-menu>
            </el-dropdown>
            <!-- <el-tabs class="tabFixed" v-if="$store.state.user.indexMenu==null" v-model="$store.state.contentTabsActiveName" @tab-click="tabSelectedHandle" @tab-remove="tabRemoveHandle">
        <el-tab-pane
          v-for="item in $store.state.contentTabs"
          :key="item.name"
          :name="item.name"
          :label="item.title"
          :closable="item.name !== 'home'"
          v-if="!(item.name == 'storehome')"
          :class="{ 'is-iframe': tabIsIframe(item.iframeURL) }" > -->
            <!-- -->
            <!-- !($store.state.roleId!=null && item.name=='home') ||  -->
            <!-- <template v-if="item.name === 'home'">
            <svg slot="label" class="icon-svg aui-content--tabs-icon-nav" aria-hidden="true"><use xlink:href="#icon-home"></use></svg>
          </template> -->
            <!-- <iframe v-if="tabIsIframe(item.iframeURL)" :src="item.iframeURL" width="100%" height="100%" frameborder="0" scrolling="yes"></iframe>
          <keep-alive v-else>
            <router-view v-if="item.name === $store.state.contentTabsActiveName" />
          </keep-alive>
        </el-tab-pane>
      </el-tabs> -->
            <!-- v-else  -->
            <!-- <el-tabs class="tabFixed" v-model="$store.state.contentTabsActiveName" @tab-click="tabSelectedHandle" @tab-remove="tabRemoveHandle">
        <el-tab-pane
          v-for="item in $store.state.contentTabs"
          :key="item.name"
          :name="item.name"
          :label="item.title"
          :closable="item.name !== 'storehome'"
          v-if="item.name!='home'"
          :class="{ 'is-iframe': tabIsIframe(item.iframeURL) }" >
          <iframe v-if="tabIsIframe(item.iframeURL)" :src="item.iframeURL" width="100%" height="100%" frameborder="0" scrolling="yes"></iframe>
          <keep-alive v-else>
            <router-view v-if="item.name === $store.state.contentTabsActiveName" />
          </keep-alive>
        </el-tab-pane>
      </el-tabs> -->

            <el-tabs
                :class="{ tabBigStyle: $store.state.mainSwitch }"
                class="tabFixed"
                v-model="$store.state.contentTabsActiveName"
                @tab-click="tabSelectedHandle"
                @tab-remove="tabRemoveHandle"
            >
                <el-tab-pane
                    v-for="item in $store.state.contentTabs"
                    :key="item.name"
                    :name="item.name"
                    :label="item.title"
                    :closable="item.name !== 'storehome'"
                    v-if="item.name && item.name !== 'home'"
                    :class="{ 'is-iframe': tabIsIframe(item.iframeURL) }"
                >
                    <template v-if="item.name === 'storehome'">
                        <svg
                            slot="label"
                            class="icon-svg aui-content--tabs-icon-nav"
                            aria-hidden="true"
                        >
                            <use xlink:href="#icon-home"></use>
                        </svg>
                    </template>
                    <iframe
                        v-if="tabIsIframe(item.iframeURL)"
                        :src="item.iframeURL"
                        width="100%"
                        height="100%"
                        frameborder="0"
                        scrolling="yes"
                    ></iframe>
                    <keep-alive v-else>
                        <router-view
                            v-if="
                                item.name === $store.state.contentTabsActiveName
                            "
                        />
                    </keep-alive>
                </el-tab-pane>
            </el-tabs>
        </template>
        <!-- 其他方式, 展示内容 -->
        <template v-else>
            <keep-alive>
                <router-view />
            </keep-alive>
        </template>
    </main>
</template>

<script>
    import { isURL } from "@/utils/validate";
    export default {
        data() {
            return {};
        },
        created() {},
        mounted() {},
        methods: {
            // tabs, 是否通过iframe展示
            tabIsIframe(url) {
                return isURL(url);
            },
            // tabs, 选中tab
            tabSelectedHandle(tab) {
                tab = this.$store.state.contentTabs.filter(
                    (item) => item.name === tab.name
                )[0];
                if (tab) {
                    this.$router.push({
                        name: tab.name,
                        params: { ...tab.params },
                        query: { ...tab.query },
                    });
                }
            },
            // tabs, 删除tab
            tabRemoveHandle(tabName) {
                if (tabName === "storehome") {
                    return false;
                }
                this.$store.state.contentTabs = this.$store.state.contentTabs.filter(
                    (item) => item.name !== tabName
                );
                if (this.$store.state.contentTabs.length <= 0) {
                    this.$store.state.sidebarMenuActiveName = this.$store.state.contentTabsActiveName =
                        "storehome";
                    return false;
                }
                // 当前选中tab被删除
                if (tabName === this.$store.state.contentTabsActiveName) {
                    // if(this.$store.state.contentTabs.length ==2 && this.$store.state.user.indexMenu!=null){
                    //   console.log(this.$store.state.contentTabs.length)
                    //   this.$router.push({ name: 'storehome' })
                    // }else if(this.$store.state.contentTabs.length ==2 && this.$store.state.user.indexMenu==null){
                    //   console.log(this.$store.state.contentTabs.length)
                    //   this.$router.push({ name: 'home' })
                    // }else{
                    this.$router.push({
                        name: this.$store.state.contentTabs[
                            this.$store.state.contentTabs.length - 1
                        ].name,
                    });
                    // }
                }
            },
            // tabs, 关闭其它
            tabsCloseOtherHandle() {
                // if(this.$store.state.user.indexMenu!=null){
                //     this.$store.state.contentTabs = this.$store.state.contentTabs.filter(item => {
                //     return item.name === 'storehome' || item.name === this.$store.state.contentTabsActiveName
                // })
                // }else{
                this.$store.state.contentTabs = this.$store.state.contentTabs.filter(
                    (item) => {
                        return (
                            item.name === "storehome" ||
                            item.name === this.$store.state.contentTabsActiveName
                        );
                    }
                );
                // }
            },
            // tabs, 关闭全部
            tabsCloseAllHandle() {
                // if(this.$store.state.user.indexMenu==null){
                this.$store.state.contentTabs = this.$store.state.contentTabs.filter(
                    (item) => item.name === "storehome"
                );
                this.$router.push({ name: "storehome" });
                // }else{
                //    this.$store.state.contentTabs = this.$store.state.contentTabs.filter(item => item.name === 'storehome')
                //   this.$router.push({ name: 'storehome' })
                // }
            },
        },
    };
</script>
<style lang="scss" scoped>
    .aui-content--tabs {
        padding-top: 0;
        border-bottom-right-radius: 12px;
        min-height: auto;
    }
    .aui-content > .el-tabs > .el-tabs__content > .el-tab-pane {
        min-height: auto;
    }

    .tabFixed > /deep/ .el-tabs__header {
        // margin: 30px 30px 0 20px !important;
        /*left: 195px !important;*/
        // padding-left: 40px !important;
        /*margin-right: 20px;*/
        /*border-top:20px solid #ECEDF1;*/
        /*border-left:20px solid #ECEDF1;*/
        top: 80px !important;
        border-bottom: 10px solid #ecedf1;
        box-shadow: 0 1px 2px 0 rgba(0, 0, 0, 0) !important;
        .el-tabs__item {
            height: 45px !important;
            line-height: 45px !important;
            font-size: 14px;
        }
    }
    .aui-sidebar--fold .tabFixed /deep/ .el-tabs__header {
        left: 64px !important;
    }
    .tabFixed > /deep/ .el-tabs__content {
        // padding: 10px !important;
        padding: 26px 20px 13px 20px !important;
    }
    .tabBigStyle /deep/ .el-tabs__header {
        display: none;
    }
</style>