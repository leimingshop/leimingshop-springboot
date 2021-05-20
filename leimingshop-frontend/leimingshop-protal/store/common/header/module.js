import { homeMember, memberHeader } from '@/api/api_06-08-01personalData'
import { logout } from '@/api/api_09login'
import Cookies  from 'js-cookie'
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
        commit('LOGOUT', {});
    },

    // 判断是否登陆
    isLogin({ commit }) {
        const auth = Cookies.get('auth');
        if (auth) {
            commit('SET_USER_LOGIN_INFO');
        }
    }

}

// 改变
let mutations = {
    LOGOUT(state, step) {
        logout().then(res => {
            Cookies.remove('auth');
            sessionStorage.removeItem('vuex')
            window.location.href = window.location.protocol + "//" + window.location.host
        }).catch(error => {
            console.log(error)
        })
    },

    // 设置用户登录信息
    SET_USER_LOGIN_INFO(state) {
        memberHeader().then(res => {
            if (res.code == 200) {
                state.userInfo = res.data
            }
        }).catch(error => {
            console.log(error)
        })
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
