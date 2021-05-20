import { homeMember, memberHeader } from '@/api/api_06-08-01personalData'
import { logout } from '@/api/api_09login'
import Cookies from 'js-cookie'

const namespaced = true
// 数据
let state = () => ({
    userInfo: { // 用户信息
        memberName: '',
        nickName: ''
    },
    shoppingCart: [], // 购物车

})

// 事件
let actions = {
    logOut({ commit }) {
        logout().then(res => {
            console.log('退出登录')
            Cookies.remove('auth');
            sessionStorage.removeItem('vuex')
            Cookies.remove('auth')
            window.location.href = window.location.protocol + "//" + window.location.host
            commit('SET_AUTH', '')
            commit('SET_USER_LOGIN_INFO', '')
        }).catch(error => {
            console.log(error)
        })
    },

    // 判断是否登陆
    isLogin({ commit, rootState }) {

        const  auth= Cookies.get('auth');
        // console.log('判断是否登录', Authorization, rootState)
        if (auth) {
            memberHeader().then(res => {
                if (res.code == 200) {
                    // state.userInfo = res.data
                    commit('SET_USER_LOGIN_INFO', res.data)
                }
            }).catch(error => {
                console.log(error)
            })
        }
    }

}

// 改变
let mutations = {
    LOGOUT(state, step) {

    },

    // 设置用户登录信息
    SET_USER_LOGIN_INFO(state, value) {
        state.userInfo = value
        console.log('设置用户登录信息')
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
