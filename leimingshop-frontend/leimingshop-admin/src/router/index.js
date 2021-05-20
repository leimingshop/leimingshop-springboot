import Vue from 'vue'
import Router from 'vue-router'
import http from '@/utils/request'
import { isURL } from '@/utils/validate'
// import localMenuList from './router-config'

Vue.use(Router)

// 页面路由(独立页面)
export const pageRoutes = [
  {
    path: '/404',
    component: () => import('@/views/pages/404'),
    name: '404',
    meta: { title: '404未找到' },
    beforeEnter (to, from, next) {
      // 拦截处理特殊业务场景
      // 如果, 重定向路由包含__双下划线, 为临时添加路由
      if (/__.*/.test(to.redirectedFrom)) {
        return next(to.redirectedFrom.replace(/__.*/, ''))
      }
      next()
    }
  },
  { path: '/login', component: () => import('@/views/pages/login'), name: 'login', meta: { title: '登录' } },
  { path: '/mypage', component: () => import('@/mypage/index'), name: 'login1', meta: { title: '登录222' }},
]

// 模块路由(基于主入口布局页面)
export const moduleRoutes = {
  path: '/',
  component: () => import('@/views/main'),
  name: 'main',
  redirect: { name: 'home' },
  meta: { title: '主入口布局' },
  children: [
    { path: '/home', component: () => import('@/views/modules/home'), name: 'home', meta: { title: '首页', isTab: true } },
    // admin端 有商品编辑，但是没有商品添加
    { path: '/mggoods-goods-list-add', component: () => import('@/views/modules/mggoods/goods/list/add'), name: 'mggoods/goods/list/add', meta: { title: '商品编辑', isTab: true } },

    { path: '/mggoods-goods-examine-add', component: () => import('@/views/modules/mggoods/goods/examine/add'), name: 'mggoods/goods/examine/add', meta: { title: '商品审核', isTab: true } },

	{ path: '/mgoperative-information-recommendList', component: () => import('@/views/modules/mgoperative/information/recommendList'), name: 'mgoperative-information-recommendList', meta: { title: '编辑相关推荐', isTab: true } },
  ]
}

const router = new Router({
  mode: 'hash',
  scrollBehavior: () => ({ y: 0 }),
  routes: pageRoutes.concat(moduleRoutes)
})

router.beforeEach((to, from, next) => {
  // 添加动态(菜单)路由
  // 已添加或者当前路由为页面路由, 可直接访问
  if (window.SITE_CONFIG['dynamicMenuRoutesHasAdded'] || fnCurrentRouteIsPageRoute(to, pageRoutes)) {
    return next()
  }
  // 获取菜单列表, 添加并全局变量保存
  http.get('/admin-api/menu/nav').then(({ data: res }) => {
    if (res.code !== 200) {
      // Vue.prototype.$message.error(res.msg)
      return next({ name: 'login' })
    }
    window.SITE_CONFIG['menuList'] = res.data
    fnAddDynamicMenuRoutes(window.SITE_CONFIG['menuList'])
    next({ ...to, replace: true })
  }).catch(() => {
    next({ name: 'login' })
  })
  // window.SITE_CONFIG['menuList'] = localMenuList.data
  // fnAddDynamicMenuRoutes(window.SITE_CONFIG['menuList'])
  // next({ ...to, replace: true })
})

/**
 * 判断当前路由是否为页面路由
 * @param {*} route 当前路由
 * @param {*} pageRoutes 页面路由
 */
function fnCurrentRouteIsPageRoute (route, pageRoutes = []) {
  var temp = []
  for (var i = 0; i < pageRoutes.length; i++) {
    if (route.path === pageRoutes[i].path) {
      return true
    }
    if (pageRoutes[i].children && pageRoutes[i].children.length >= 1) {
      temp = temp.concat(pageRoutes[i].children)
    }
  }
  return temp.length >= 1 ? fnCurrentRouteIsPageRoute(route, temp) : false
}

/**
 * 添加动态(菜单)路由
 * @param {*} menuList 菜单列表
 * @param {*} routes 递归创建的动态(菜单)路由
 */
function fnAddDynamicMenuRoutes (menuList = [], routes = []) {
  var temp = []
  for (var i = 0; i < menuList.length; i++) {
    if (menuList[i].children && menuList[i].children.length >= 1) {
      temp = temp.concat(menuList[i].children)
      continue
    }
    // 组装路由
    var route = {
      path: '',
      component: null,
      name: '',
      meta: {
        ...window.SITE_CONFIG['contentTabDefault'],
        menuId: menuList[i].id,
        title: menuList[i].name
      }
    }
    // eslint-disable-next-line
    let URL = (menuList[i].url || '').replace(/{{([^}}]+)?}}/g, (s1, s2) => eval(s2)) // URL支持{{ window.xxx }}占位符变量
    if (isURL(URL)) {
      route['path'] = route['name'] = `i-${menuList[i].id}`
      route['meta']['iframeURL'] = URL
    } else {
      URL = URL.replace(/^\//, '').replace(/_/g, '-')
      route['path'] = route['name'] = URL.replace(/\//g, '-')
      // route['component'] = () => import(`@/views/modules/${URL}`)
      route['component'] = resolve => require([`@/views/modules/${URL}`],resolve);
    }
    routes.push(route)
  }
  if (temp.length >= 1) {
    return fnAddDynamicMenuRoutes(temp, routes)
  }
  // 添加路由
  router.addRoutes([
    {
      ...moduleRoutes,
      name: 'main-dynamic-menu',
      children: routes
    },
    { path: '*', redirect: { name: '404' } }
  ])
  window.SITE_CONFIG['dynamicMenuRoutes'] = routes
  window.SITE_CONFIG['dynamicMenuRoutesHasAdded'] = true
}

export default router
