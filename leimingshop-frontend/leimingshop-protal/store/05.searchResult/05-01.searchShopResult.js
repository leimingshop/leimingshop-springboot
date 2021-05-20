
import {
    searchStore
} from '@/api/api_05searchResult'


const namespaced = true

// 数据
let state = () => ({
    storeVOList: [], // 店铺列表
    totalCount: 0, // 总条数
    pageSize: 20, // 每页条数
    pageNo: 1, // 当前页
    dataLoading: false, // 列表页加载中
})

// 事件
let actions = {

    async handleSearchShop({ commit, state }, params) {
        state.dataLoading = true
        const res = await searchStore(params)
        state.dataLoading = false

        if (res && res.code == 200) {
            commit('HANDLE_SEARCH_SHOP', res.data)
        }
    },
}

// 改变
let mutations = {
    // 切换分页
    HANDLE_CHANGE_PAGENO(state, data) {
        state.pageNo = parseInt(data)
    },

    // 商品搜索列表
    HANDLE_SEARCH_SHOP(state, data) {
        state.storeVOList = data.storeVOList
        state.totalCount = parseInt(data.totalCount)
        state.pageSize = parseInt(data.pageSize)
        state.pageNo = parseInt(data.pageNo)
    },
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
