import {
    aftersaleApplyCancel,
    aftersaleApplyDetail,
    aftersaleApplyProcess,
    aftersaleApplyRecord,
    aftersaleApplySave,
    aftersaleAvailable
} from '@/api/api_06-03.afterSale'

const namespaced = true

// 数据 测试账号（15238448329，123456）
let state = () => ({
    applyList: [], // 申请列表
    recordList: [], // 记录列表
    totalCount: 0, // 总记录数
    page: 1, // 当前页码，从1开始
    limit: 6, // 每页显示记录数
    unionQuery: '', // 售后记录列表 查询字段
    orderSn: '', // 申请售后服务列表 查询字段
    applyDataLoading: false,
    recordDataLoading: false
})

// 事件
let actions = {
    async handleAftersaleApplyCancel({ commit, state }, params) {
        const res = await aftersaleApplyCancel(params)
        commit('HANDLEAFTERSALEAPPLYCANCEL')
    },

    async handleAftersaleApplyDetail({ commit, state }, params) {
        const res = await aftersaleApplyDetail(params)
        commit('HANDLEAFTERSALEAPPLYDETAIL')
    },

    async handleAftersaleApplyProcess({ commit, state }, params) {
        const res = await aftersaleApplyProcess(params)
        commit('HANDLEAFTERSALEAPPLYPROCESS')
    },

    // 售后服务申请记录列表
    async handleAftersaleApplyRecord({ commit, state }) {
        console.log('售后服务申请记录，state')
        let params = {
            page: state.page,
            limit: state.limit,
            unionQuery: state.unionQuery
        }

        state.recordDataLoading = true
        const res = await aftersaleApplyRecord(params)
        state.recordDataLoading = false

        if (res && res.code == 200) {
            commit('HANDLE_AFTERSALE_APPLY_RECORD', res.data)
        }

    },

    // 订单可申请售后服务列表
    async handleAftersaleAvailable({ commit, state }) {
        let params = {
            page: state.page,
            limit: state.limit,
            orderSn: state.orderSn
        }

        state.applyDataLoading = true
        const res = await aftersaleAvailable(params)
        state.applyDataLoading = false

        if (res && res.code == 200) {
            commit('HANDLE_AFTERSALE_AVAILABLE', res.data)
        }
    }
}

// 改变
let mutations = {
    // 切换分页
    HANDLE_CHANGE_PAGENO(state, data) {
        state.page = parseInt(data)
    },

    // 申请售后服务列表 查询字段
    HANDLE_APPLY_SEARCH_DATA(state, data) {
        state.orderSn = data
    },

    // 售后记录列表 查询字段
    HANDLE_RECORD_SEARCH_DATA(state, data) {
        state.unionQuery = data
    },

    HANDLEAFTERSALEAPPLYCANCEL(state, data) {

    },
    HANDLEAFTERSALEAPPLYDETAIL(state, data) {

    },
    HANDLEAFTERSALEAPPLYPROCESS(state, data) {

    },
    HANDLE_AFTERSALE_APPLY_RECORD(state, data) {
        state.recordList = data.list || []
        state.totalCount = data.total
    },
    HANDLE_AFTERSALE_AVAILABLE(state, data) {
        state.applyList = data.list || []
        state.totalCount = data.total
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

