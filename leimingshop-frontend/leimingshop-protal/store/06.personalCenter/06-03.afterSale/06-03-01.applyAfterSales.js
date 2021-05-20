import {
    afterSaleOrderInfo,
    afterApplySave,
    orderApplyReason,
    addressList
} from '@/api/api_06-03.afterSale'

import axios from 'axios'

// import router from '@/router' ssr注释

const namespaced = true

// 数据 测试账号（15238448329，123456）
let state = () => ({
    orderInfo: null,
    aftersalePicList: [],
    applyReasonList: [],
    addressList: [],
    afterSaleAddress: {}, // 商品售后地址
    memberAddress: {}, // 从地址弹窗列表选中的用户地址， 用于【弹窗 - 修改地址信息】
    memberAddressId: '', // 用于【弹窗 - 地址列表页】 默认选中样式
    orderInfoLoading: false,
    applySaveLoading: false,
    applyReasonLoading: false,
    addressListLoading: false
})

// 事件
let actions = {

    // 获取订单商品信息
    async handleOrderInfo({ commit, state }, params) {
        commit('HANDLE_LOADINT_STATUS', ['orderInfoLoading', true])
        const res = await afterSaleOrderInfo(params)
        commit('HANDLE_LOADINT_STATUS', ['orderInfoLoading', false])

        if (res && res.code == 200) {
            commit('HANDLE_ORDER_INFO', res.data)
        }
    },

    // 获取售后原因
    async handleOrderApplyReason({ commit, state }, params) {
        commit('HANDLE_LOADINT_STATUS', ['applyReasonLoading', true])
        const res = await orderApplyReason(params)
        commit('HANDLE_LOADINT_STATUS', ['applyReasonLoading', false])

        if (res && res.code == 200) {
            commit('HANDLE_ORDER_APPLY_REASON', res.data)
        }
    },

    // 保存售后申请
    async handleAfterSaleApplySave({ commit, state }, { applyInfo, title }) {

        commit('HANDLE_LOADINT_STATUS', ['applySaveLoading', true])
        const res = await afterApplySave(applyInfo)
        commit('HANDLE_LOADINT_STATUS', ['applySaveLoading', false])
        if (res && res.code == 200) {
            this._vm.$Message.success(res.msg)
            this._vm.$nuxt._router.replace({
                path: '/personalCenterBase/applyAfterSalesSuccess',
                query: { title }
            })
        } else {

        }
    },

    // 地址列表
    async handleAddressList({ commit, state }) {
        let params = {
            page: 1,
            limit: 1000
        }

        commit('HANDLE_LOADINT_STATUS', ['addressListLoading', true])
        const res = await addressList(params)
        commit('HANDLE_LOADINT_STATUS', ['addressListLoading', false])

        if (res && res.code == 200) {
            commit('HANDLE_ADDRESS_LIST', res.data.list)
        }
    }
}

// 改变
let mutations = {
    HANDLE_LOADINT_STATUS(state, data) {
        state[data[0]] = data[1]
    },

    HANDLE_ORDER_INFO(state, data) {
        state.orderInfo = data
        state.afterSaleAddress = {
            receiver: data.contacts,
            receiverPhone: data.contactsPhone,
            receiverAddress: `${data.address}\n${data.areaInfo}`
        }
    },

    HANDLE_RESET_PICLIST(state) {
        state.aftersalePicList = []
    },

    HANDLE_ORDER_APPLY_REASON(state, data) {
        state.applyReasonList = data
    },

    HANDLE_ADDRESS_LIST(state, data) {
        state.addressList = data
    },

    // 处理从地址列表页选中的数据 or 修改过地址以后的数据
    HANDEL_AFTER_ADDRESS(state, data) {
        state.afterSaleAddress = {
            receiver: data.trueName,
            receiverPhone: data.mobPhone,
            receiverAddress: `${data.address}\n${data.areaInfo}`
        }

        state.orderInfo = Object.assign({}, state.orderInfo, data)
    },

    // 从地址列表页选中回调
    HANDEL_MEMBER_ADDRESS(state, data) {
        state.memberAddress = data
        state.memberAddressId = data.id
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

