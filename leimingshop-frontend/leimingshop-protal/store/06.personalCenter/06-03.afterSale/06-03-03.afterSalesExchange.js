import {
    aftersaleApplyDetail,
    aftersaleApplyProcess,
    aftersaleApplyCancel,
    sellerDeliverySave,
    orderConfirmReceipt
} from '@/api/api_06-03.afterSale'


const namespaced = true

// 数据
let state = () => ({
    aftersaleApplyDetail: {}, // 服务单详情
    progressDetails: {}, // 进度详情
    detailsLoading: false,
    progressLoading: false,
    applyCancelLoading: false,
    saveLogisticsLoading: false,
    confirmReceiptLoading: false
})

// 事件
let actions = {
    // 服务单详情
    async handleAftersaleApplyDetail({ commit, state }, params) {
        state.detailsLoading = true
        const res = await aftersaleApplyDetail(params)
        state.detailsLoading = false

        if (res && res.code == 200) {
            commit('HANDLE_AFTERSALE_APPLY_DETAIL', res.data)
        }
    },

    // 售后进度
    async handleAftersaleProgress({ commit, state }, params) {
        state.progressLoading = true
        const res = await aftersaleApplyProcess(params)
        state.progressLoading = false

        if (res && res.code == 200) {
            commit('HANDLE_AFTERSALE_PROGRESS', res.data)
        }
    },

    // 取消申请
    async handleAftersaleApplyCancel({ commit, state }, params) {
        state.applyCancelLoading = true
        const res = await aftersaleApplyCancel(params)
        state.applyCancelLoading = false

        if (res && res.code == 200) {
            this._vm.$Message.success(res.msg)
            commit('HANDLE_RESET_AFTERSALE_DETAILS')
        }
    },

    // 上传物流信息
    handleSellerDeliverySave({ commit, state }, params) {

        return new Promise(async (resolve, reject) => {
            state.saveLogisticsLoading = true
            const res = await sellerDeliverySave(params)
            state.saveLogisticsLoading = false

            if (res && res.code == 200) {
                this._vm.$Message.success(res.msg)
                commit('HANDLE_RESET_AFTERSALE_DETAILS')
                resolve(true)
            } else {
                resolve(false)
            }
        })
    },

    // 确认收货
    async handleConfirmReceipt({ commit, state }, params) {
        state.confirmReceiptLoading = true
        const res = await orderConfirmReceipt(params)
        state.confirmReceiptLoading = false
        if (res && res.code == 200) {
            this._vm.$Message.success(res.msg)
            commit('HANDLE_RESET_AFTERSALE_DETAILS')
        }
    }
}

// 改变
let mutations = {

    HANDLE_AFTERSALE_APPLY_DETAIL(state, data) {
        state.aftersaleApplyDetail = data

    },

    HANDLE_AFTERSALE_PROGRESS(state, data) {
        state.progressDetails = data
    },

    HANDLE_RESET_AFTERSALE_DETAILS(state) {
        let params = {
            aftersaleId: state.aftersaleApplyDetail.aftersaleSn,
            aftersaleType: state.aftersaleApplyDetail.aftersaleType
        }
        this.dispatch('afterSalesExchange/handleAftersaleApplyDetail', params)
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

