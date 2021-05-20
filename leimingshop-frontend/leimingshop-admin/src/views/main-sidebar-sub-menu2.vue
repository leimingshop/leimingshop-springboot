<template>
  <el-submenu
    class="lilevel"
    v-if="menu.children && menu.children.length >= 1"
    :index="menu.id + ''"
    :popper-class="`site-sidebar--${$store.state.sidebarLayoutSkin}-popper`">
    <template slot="title">
      <!-- <icon-svg :name="menu.icon || ''" class="site-sidebar__menu-icon"></icon-svg> -->
       <svg class="icon-svg aui-sidebar__menu-icon" aria-hidden="true"><use :xlink:href="`#${menu.icon}`"></use></svg>
      <span>{{ menu.name }}</span>
       
    </template>
    <sub-menu
      v-for="(item) in menu.children"
      :key="item.id"
      :menu="item"
      :dynamicMenuRoutes="dynamicMenuRoutes">
    </sub-menu>
  </el-submenu>
  <el-menu-item  class="lileve2" v-else :index="menu.id + ''" @click="gotoRouteHandle(menu)">
    <!-- <icon-svg :name="menu.icon || ''" class="site-sidebar__menu-icon"></icon-svg> -->
    <svg class="icon-svg aui-sidebar__menu-icon" aria-hidden="true"><use :xlink:href="`#${menu.icon}`"></use></svg>
    <span>{{ menu.name }}</span>
  </el-menu-item>
</template>

<script>
import SubMenu from './main-sidebar-sub-menu2'
export default {
  name: 'sub-menu',
  props: {
    menu: {
      type: Object,
      required: true
    },
    dynamicMenuRoutes: {
      type: Array,
      required: true
    }
  },
  components: {
    SubMenu
  },
  methods: {
    // 通过menuId与动态(菜单)路由进行匹配跳转至指定路由
    gotoRouteHandle (menu) {
//  	localStorage.setItem("nogochooseItem","T")
      var route = this.dynamicMenuRoutes.filter(item => item.meta.menuId === menu.id)
      if (route.length >= 1) {
        // alert(route[0].name)
        this.$router.push({ name: route[0].name })
      }
    }
  }
}
</script>
