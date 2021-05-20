import {
    storeClass,
    goodsRecommend
} from '@/api/api_02shopIndex'

import { handleGroup } from '@/utils/array-handle'

const namespaced = true

// 数据
let state = () => ({
    shopClassifyList: [], // 店铺 - 宝贝分类  （测试id 1191626469961400321）
    dataLoading: true, // 店铺 - 店铺分类列表加载 - loading
    recommendList: [], // 店铺 - 推荐列表 （测试id 1191626469961400321）
    recommendLoading: true, // 店铺 - 推荐列表 - loading
    carouselItemNum: 1 // 店铺 - 推荐列表 - 轮播盒子数量,一个盒子5张图
})

// 事件
let actions = {
    async handleStoreClass({ commit, state }, params) {
        state.dataLoading = true
        const res = await storeClass(params)
        state.dataLoading = false

        if (res && res.code == 200) {
            commit('HANDLESTORECLASS', res.data)
        }
    },

    async handleGoodsRecommend({ commit, state }, params) {
        state.recommendLoading = true
        const res = await goodsRecommend(params)
        state.recommendLoading = false

        if (res && res.code == 200) {
            commit('HANDLEGOODSRECOMMEND', res.data)
        }
    },
}

// 改变
let mutations = {
    HANDLESTORECLASS(state, data) {
        state.shopClassifyList = data
    },

    HANDLEGOODSRECOMMEND(state, data) {
        let temp = handleGroup(data, 5)
        state.recommendList = temp['newArray']
        state.carouselItemNum = temp['count']
    }
}

// 获取
let getters = {
}

export default {
    namespaced,
    actions,
    state,
    mutations,
    getters
}