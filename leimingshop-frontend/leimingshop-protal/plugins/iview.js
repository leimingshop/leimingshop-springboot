import Vue from 'vue'
import iView from 'iview'
import locale from 'iview/dist/locale/en-US' // Change locale, check node_modules/iview/dist/locale
import VueLazyload from 'vue-lazyload'
import VueWechatTitle from 'vue-wechat-title'
import vueFilter from '@/utils/filter'
import 'iview/dist/styles/iview.css'
// import $ from "jquery"
// window.$ = $;
for (let key in vueFilter) {
  Vue.filter(key, vueFilter[key])
}
Vue.use(VueWechatTitle)
Vue.prototype.$imgURL = 'http://leimingshop'
Vue.use(iView, {
  locale
})
// 自定义指令
import loadingDirective from '../directive/loadingDirective'

Vue.use(loadingDirective)
Vue.use(VueLazyload, {
  loading: require('static/img/common/loading/200_200.png'), //加载中图片，一定要有，不然会一直重复加载占位图
  error: require('static/img/common/loading/200_200.png') //加载失败图片
});
