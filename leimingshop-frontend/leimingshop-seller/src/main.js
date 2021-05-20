import Vue from 'vue'
import Element from 'element-ui'
import App from '@/App'
import i18n from '@/i18n'
import router from '@/router'
import store from '@/store'
import '@/icons'
import '@/element-ui/theme/index.css'
import '@/assets/scss/aui.scss'
import http from '@/utils/request'
import { hasPermission } from '@/utils'
import cloneDeep from 'lodash/cloneDeep'
import "@/utils/importJs.js"
import Clipboard from 'clipboard';
import VCharts from 'v-charts'
//大图预览带分页
import VueImageSwipe from 'vue-image-swipe'
import 'vue-image-swipe/dist/vue-image-swipe.css'
Vue.use(VueImageSwipe);
//图片放大功能
import preview from 'vue-photo-preview'
import 'vue-photo-preview/dist/skin.css'
import $ from "jquery"
window.$ = $;
Vue.use(preview)
// 过滤器
import vueFilter from '@/utils/filter'
for (let key in vueFilter){
  Vue.filter(key,vueFilter[key])
}

import VueDND from 'awe-dnd'
Vue.use(VueDND)
//控制列表是否放大的组件
import mainSwitch from '@/components/main-switch.vue'
Vue.component('mainSwitch', mainSwitch)

//控制是否显示提示信息的组件
import mainTipsMessage from '@/components/main-message.vue'
Vue.component('mainTipsMessage', mainTipsMessage)

Vue.config.productionTip = false

Vue.use(VCharts)
Vue.use(Element, {
  size: 'default',
  i18n: (key, value) => i18n.t(key, value)
})

// 挂载全局
Vue.prototype.$http = http
Vue.prototype.$hasPermission = hasPermission
// Vue.prototype.$imgDomain = 'http://62.234.0.169:8888'//图片前缀
Vue.prototype.$imgDomain = window.SITE_CONFIG['imgURL'] ;


// 保存整站vuex本地储存初始状态
window.SITE_CONFIG['storeState'] = cloneDeep(store.state)

new Vue({
  i18n,
  router,
  store,
  Clipboard,
  render: h => h(App)
}).$mount('#app')
