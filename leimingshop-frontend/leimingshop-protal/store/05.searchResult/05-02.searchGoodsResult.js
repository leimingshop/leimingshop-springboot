import {
    filterGoods,
    searchGoods,
    hotRecommendList,
    collectBillsReduce
} from '@/api/api_05searchResult'

const namespaced = true

// 数据
let state = () => ({
    hasSearchList: true, //首次加载 是否有搜索结果列表
    brandVOList: [], // 品牌列表
    goodsLabelVOList: [], //标签列表
    goodsAttrVOList: [], //商品属性列表
    goodsVOList: [], // 商品列表
    totalCount: 0, // 总条数
    pageSize: 20, // 每页条数
    pageNo: 1, // 当前页
    categoryLoading: true, // 商品类别加载中
    dataLoading: true, // 列表页加载中
    recommendList: [], // 热门推荐列表
    recommendLoading: true, //热门推荐加载中
    test: 1
})

// 事件
let actions = {

    async handleSearchGoods({ commit, state }, params) {
        state.categoryLoading = true
        // console.log(params, 'params')
        const res = await searchGoods(params)
        // console.log('我获取到结果了')
        state.categoryLoading = false
        if (res && res.code == 200) {
            commit('HANDLE_SEARCH_GOODS', res.data)
        }
    },

    async handleFilterGoods({ commit, state }, params) {
        state.dataLoading = true
        const res = await filterGoods(params)
        state.dataLoading = false

        if (res && res.code == 200) {
            commit('HANDLE_FILTER_GOODS', res.data)
        }
    },

    async hanldeRecommendList({ commit, state }) {
        state.recommendLoading = true
        const res = await hotRecommendList()
        state.recommendLoading = false

        if (res && res.code == 200) {
            commit('HANDLE_HOTRE_COMMEND_LIST', res.data)
        }
    },

    // 满减凑单列表
    async handleCollectBillsReduce({ commit, state }, params) {
        state.dataLoading = true
        const res = await collectBillsReduce(params)
        state.dataLoading = false

        if (res && res.code == 200) {
            commit('HANDLE_COLLECT_BILLS_REDUCE', res.data)
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
    HANDLE_SEARCH_GOODS(state, data) {
        console.log(data, '商品搜索列表')
        state.brandVOList = data.brandVOList
        state.goodsLabelVOList = data.goodsLabelVOList
        state.goodsAttrVOList = data.goodsAttrVOList
        state.hasSearchList = parseInt(data.totalCount) == 0 ? false : true
    },


    // 商品过滤列表
    HANDLE_FILTER_GOODS(state, data) {
        state.goodsVOList = data.goodsVOList
        state.totalCount = parseInt(data.totalCount)
        state.pageSize = parseInt(data.pageSize)
        state.pageNo = parseInt(data.pageNo)
    },

    // 处理面包屑
    HANDLE_BREADCRUMB_LIST(state, data) {
        state.breadcrumbList = data
    },

    /*
        * 通过该方法注入数据
        * 使用方法 this.commit( 'searchGoodsResult/HANDLE_INJECT_DATA', injectData, {root: true} )
    */
    HANDLE_INJECT_DATA(state, injectData) {
        for (let item in injectData) {
            state[item] = injectData[item]
        }
    },

    // 热门推荐列表
    HANDLE_HOTRE_COMMEND_LIST(state, data) {
        state.recommendList = data
    },

    // 满减凑单列表
    HANDLE_COLLECT_BILLS_REDUCE(state, data) {
        let goodsVOList = data.frontCouponsGoodsVOList

        goodsVOList.map((item, index) => {
            item.id = item.goodsId
            item.specId = item.goodsVO.specId
            item.goodsMainPicture = item.specMainPicture
            item.goodsName = item.specName
            item.goodsSubTitle = item.goodsVO.goodsSubTitle
            item.storeType = item.goodsVO.storeType
            item.storeName = item.goodsVO.storeName
            item.storeId = item.goodsVO.storeId
            item.evaluateCount = item.goodsVO.evaluateCount
            item.goodsSaleNum = item.specSaleNum
        })

        state.goodsVOList = goodsVOList
        state.totalCount = parseInt(data.totalCount)
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