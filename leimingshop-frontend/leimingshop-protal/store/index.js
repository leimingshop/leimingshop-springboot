import goodsClass from './03.goodsClass/index.js'
import searchResult from './05.searchResult/index.js' // 05
import personalCenterBase from './06.personalCenter/06.personalCenterBase.js'
import evaluationSheet from './06.personalCenter/06-02.evaluationSheet.js'    //个人中心 评价晒单-待评价
import afterSale from './06.personalCenter/06-03.afterSale'
import addressManagement from './06.personalCenter/06-04.addressManagement.js' //个人中心 地址管理
import myPoints from './06.personalCenter/06-05myPoints.js' //个人中心 我的积分
import myOrders from './06.personalCenter/06-07.myOrders.js' //个人中心 我的订单
import shoppingCarIndex from './04.shoppingCar/index.js'
import register from './10.register/module.js'
import subject from './subject.js'
import Cookies from 'js-cookie'


export const strict = false
export const state = () => ({
    auth: '',
    rediskey:''
})

export const mutations = {
    SET_AUTH(state, user) {
        Cookies.set('auth', `Bearer ${user}`,{ expires: 7 })
        state.auth = user
    },
    SET_REDISKEY(state,rediskey) {
            Cookies.set('rediskey', rediskey)
            state.rediskey = rediskey
    }
}
export const actions = {
    nuxtServerInit({ commit }, { req }) {
        console.log('nustServerInit', req.headers.cookie,)
        let cookie = req.headers.cookie
        if (cookie && cookie.auth) {
            commit("SET_AUTH", cookie.auth)
            commit("rediskey",cookie.rediskey)
        }
    }
}
export const modules = {
    ...goodsClass, // 商品相关a
    ...searchResult, // 搜索结果
    personalCenterBase,
    evaluationSheet,
    ...afterSale,
    addressManagement,
    myPoints,
    myOrders,
    shoppingCarIndex, // 购物车
    register,
    subject
}
