import loadMore from './dataLoading.vue'    //下滑加载更多
import searchFirstLevel from './searchFirstLevel.vue'    //一级公共搜索头部
import searchSecondLevel from './searchSecondLevel.vue'    //二级公共搜索头部
export default {
  install (Vue) {
    Vue.component(loadMore.name, loadMore),
    Vue.component(searchFirstLevel.name, searchFirstLevel),
    Vue.component(searchSecondLevel.name, searchSecondLevel)
  }
}
