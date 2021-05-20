<template>
    <aside
        :class="[
            'aui-sidebar',
            `aui-sidebar--${$store.state.sidebarLayoutSkin}`,
            $store.state.sidebarFold ? 'aui-sidebar1' : 'aui-sidebar2',
        ]"
    >
        <div class="aui-sidebar__inner">
            <!-- <span style="color:red">{{$store.state.sidebarMenuActiveName}}</span> -->
            <el-menu
                :default-active="$store.state.sidebarMenuActiveName"
                :collapse="$store.state.sidebarFold"
                :unique-opened="true"
                :collapseTransition="false"
                class="aui-sidebar__menu"
            >
                <!-- <sub-menu v-for="menu in $store.state.sidebarMenuList" :key="menu.id" :menu="menu" /> -->
                <!-- <span class="aui-sidebar__menu aui-sidebar--default aui-sidebar2"  v-for="(item,index) in this.menuName">
              <el-menu-item  :index="item.id + ''"  @click="$router.push({ name: item.url })" v-if="parentItem.children.length==0"   class="lileve2">
                <div>
                  <svg slot="label" class="icon-svg aui-content--tabs-icon-nav aui-sidebar__menu-icon" aria-hidden="true"><use :xlink:href="`#${item.icon}`"></use></svg>
                  <span slot="title">{{item.name}}</span>
                </div>
              </el-menu-item>
        </span> -->
                <!-- <el-menu-item index="home" @click="$router.push({ name: window.SITE_CONFIG['menuList'][0].children[0].url })" v-if="parentItem.children.length==0"   class="lileve2">
              <div>
                 <svg slot="label" class="icon-svg aui-content--tabs-icon-nav aui-sidebar__menu-icon" aria-hidden="true"><use xlink:href="#icon-home"></use></svg>
              <span slot="title">首页</span>
              </div>
          </el-menu-item> -->

                <!-- <el-menu-item index="home" @click="$router.push({ name: 'personal' })" v-if="parentItem.children.length==0"   class="lileve2">
                <div>
                    <svg slot="label" class="icon-svg aui-content--tabs-icon-nav aui-sidebar__menu-icon" aria-hidden="true"><use xlink:href="#icon-user"></use></svg>
                    <span slot="title">个人中心</span>
                </div>
            </el-menu-item> -->

                <sub-menu
                    v-for="menu in parentItem.children"
                    :key="menu.id"
                    :menu="menu"
                    :dynamicMenuRoutes="dynamicMenuRoutes"
                >
                </sub-menu>
            </el-menu>
        </div>
        <el-menu
            class="aui-navbar__menu mr-auto bottomContraction"
            mode="horizontal"
        >
            <el-menu-item
                index="1"
                @click="$store.state.sidebarFold = !$store.state.sidebarFold"
            >
                <!-- <svg class="icon-svg aui-navbar__icon-menu aui-navbar__icon-menu--switch" aria-hidden="true"><use xlink:href="#icon-outdent"></use></svg> -->
                <div
                    :class="
                        $store.state.sidebarFold
                            ? 'sidebarFoldTrue'
                            : 'sidebarFoldFalse'
                    "
                >
                    <i class="el-submenu__icon-arrow el-icon-arrow-down"></i>
                </div>
            </el-menu-item>
        </el-menu>
    </aside>
</template>

<script>
    import SubMenu from "./main-sidebar-sub-menu2";
    // import { isURL } from '@/utils/validate'
    export default {
        data() {
            return {
                menuList: [],
                dynamicMenuRoutes: [],
                menuName: [],
            };
        },
        props: ["parentItem"],
        components: {
            SubMenu,
        },
        created() {
            this.$store.state.sidebarMenuList = window.SITE_CONFIG["menuList"];
            let menuList = window.SITE_CONFIG["menuList"] || [];
            let parentArr = [];
            menuList.forEach((item, index) => {
                parentArr.push(item);
            });
            this.$emit("setParentArr", parentArr);
            this.dynamicMenuRoutes = window.SITE_CONFIG["dynamicMenuRoutes"] || [];
            // this.routeHandle(this.$route)
            // this.indexMenu();
        },
        methods: {
            // indexMenu(){
            //      var name= window.SITE_CONFIG['menuList'][0].children[0].name;
            //      if(name=='首页'){
            //        this.menuName= window.SITE_CONFIG['menuList'][0].children
            //      }else{
            //       this.menuName= window.SITE_CONFIG['menuList'][0].children
            //       let reg=new RegExp('/','g')//g代表全部
            //       let index=window.SITE_CONFIG['menuList'][0].children;
            //       console.log(index.length)
            //       for(let i=0;i<=index.length-1;i++){
            //           this.menuName[i].url=index[i].url.replace(reg,'-');
            //           console.log(this.menuName[i])
            //       }
            //      }
            // }
            // 路由操作
            // routeHandle (route) {
            //   if (route.meta.isTab && this.$store.state.mainTabs) {
            //     // tab选中, 不存在先添加
            //     let tab = this.$store.state.mainTabs.filter(item => item.name === route.name)[0]
            //     if (!tab) {
            //       if (route.meta.isDynamic) {
            //         route = this.dynamicMenuRoutes.filter(item => item.name === route.name)[0]
            //         if (!route) {
            //           return console.error('未能找到可用标签页!')
            //         }
            //       }
            //       tab = {
            //         menuId: route.meta.menuId || route.name,
            //         name: route.name,
            //         title: route.meta.title,
            //         type: isURL(route.meta.iframeUrl) ? 'iframe' : 'module',
            //         iframeUrl: route.meta.iframeUrl || ''
            //       }
            //       this.$store.state.mainTabs = this.$store.state.mainTabs.concat(tab)
            //     }
            //     this.$store.state.menuActiveName = tab.menuId + ''
            //     this.$store.state.mainTabsActiveName = tab.name
            //   }
            // }
        },
    };
</script>
<style lang="scss" scoped>
    @import "@/element-ui/theme-variables.scss";
    // @import "@/assets/scss/aui.scss";
    .aui-sidebar {
        margin: 0px;
        background-color: #1d2033 !important;
        top: 80px;
    }
    .el-dropdown-menu__item--divided:before,
    .el-menu,
    .el-menu--horizontal > .el-menu-item:not(.is-disabled):focus,
    .el-menu--horizontal > .el-menu-item:not(.is-disabled):hover,
    .el-menu--horizontal > .el-submenu .el-submenu__title:hover {
        background-color: #1d2033;
        color: #fff;
    }

    .el-menu-item:focus,
    .el-menu-item:hover {
        outline: 0;
        background-color: #2e3041;
    }

    .aui-sidebar__inner {
        width: 100%;
        position: relative;
        .aui-sidebar__menu {
            margin-top: 7px;
            width: 100%;
        }
    }
    .aui-sidebar__inner::-webkit-scrollbar {
        display: none;
    }
    .aui-sidebar__menu {
        // margin-left: 20px;
    }
    .aui-sidebar2 {
        // width: 280px;
        /deep/ .el-submenu__title {
            height: 45px;
            line-height: 45px;
            font-size: 16px;
            color: rgba(51, 51, 51, 1);
            padding-left: 25px !important;
            /deep/ .el-submenu__icon-arrow {
                width: 13px;
                height: 13px;
            }
        }
    }
    /deep/ .is-active > .el-submenu__title {
        color: $--color-primary;
        i {
            color: $--color-primary;
        }
    }

    /deep/ .el-menu--inline {
        .el-menu-item {
            padding-left: 38px !important;
            box-sizing: border-box;
            /*height: 68px;
                 line-height: 68px;*/
            height: 45px;
            line-height: 45px;
            font-size: 14px;
            color: rgba(102, 102, 102, 1);
            min-width: 190px;
        }
        .is-active {
            border-left: 4px solid $--color-primary !important;
            padding-left: 34px !important;
            /*color: #3F51AF;*/
            color: #fff !important;
            background-color: #e6f4fc;
        }
    }
    .bottomContraction {
        position: absolute;
        bottom: 30px;
        right: 0;
        z-index: 2;
        .el-menu-item {
            padding-right: 0;
            height: auto;
        }
        .el-icon-arrow-down:before {
            content: "\E603";
        }
        .sidebarFoldTrue,
        .sidebarFoldFalse {
            width: 38px;
            height: 36px;
            border-radius: 20px 0 0 20px;
            position: relative;
        }
        .sidebarFoldTrue {
            background: $--color-primary;
            color: #ffffff;
        }
        .sidebarFoldFalse {
            background: rgba(240, 240, 240, 1);
            color: #979797;
        }
        .el-icon-arrow-down {
        }
        .sidebarFoldTrue .el-icon-arrow-down {
            transform: rotateZ(-90deg);
        }
        .sidebarFoldFalse .el-icon-arrow-down {
            transform: rotateZ(90deg);
        }
    }

    // 如果沒有三级菜单，单独处理
    .aui-sidebar2 .aui-sidebar__menu > .lileve2 {
        /*color: #999BA9;*/
        padding-left: 45px !important;
        font-size: 14px;
        height: 45px;
        line-height: 45px;
    }
    .el-menu-item {
        color: #999ba9;
    }
    .el-menu-item.is-active {
        color: #fff;
    }
</style>
