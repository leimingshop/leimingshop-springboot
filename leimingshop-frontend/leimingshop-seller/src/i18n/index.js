import Vue from 'vue'
import VueI18n from 'vue-i18n'
import Cookies from 'js-cookie'
import zhCNLocale from 'element-ui/lib/locale/lang/zh-CN'
import zhTWLocale from 'element-ui/lib/locale/lang/zh-TW'
import enLocale from 'element-ui/lib/locale/lang/en'
import zhCN from './zh-CN'
import zhTW from './zh-TW'
import enUS from './en-US'

Vue.use(VueI18n)

export const messages = {
  'zh-CN': {
    '_lang': '简体中文',
    ...zhCN,
    ...zhCNLocale
  },
  'zh-TW': {
    '_lang': '繁體中文',
    ...zhTW,
    ...zhTWLocale
  },
  'en-US': {
    '_lang': 'English',
    ...enUS,
    ...enLocale
  }
}

export default new VueI18n({
  locale: Cookies.get('language') || 'zh-CN',
  messages
})
