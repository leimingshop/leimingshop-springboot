import {
    wechatPcLogin,
    loginMobileBind
} from '@/api/api_09login.js'

const namespaced = true
// 数据
let state = () => ({
    uuid: '',
    wechat_state: {},
    sendCode_state: {},
    CODE: '',
    bindPhone_state: {},
    captcha: '',
})

// 事件
let actions = {
    getCaptcha({ //获取图形验证码
        commit
    }) {
        var random = ''

        function getRandom(num) {
            random = Math.floor((Math.random() + Math.floor(Math.random() * 9 + 1)) * Math.pow(10, num - 1));
        }
        getRandom(10);
        commit('SET_LOGIN_CAPTCHA', random);
    },

    captchaLogin({ commit }, data) { //账号密码图形验证码登陆
        commit('LOGIN', data)
    },

    getOpenID({ commit }, data) {//微信登录获取登录状态
        wechatPcLogin(data).then(res => {
            commit('SET_WECHAT_STATE', { res: res, data: data });
        }).catch(error => {
            console.log(error)
            commit('SET_WECHAT_STATE', error);
        })
    },
    bindSendCode({ commit }, data) {//微信绑定手机获取短信验证码
        loginMobileBind(data).then(res => {
            commit('SET_CODE', res)
        }).catch(error => {
            console.log(error)
        })
    },
    bindPhone({ commit, state }, data) {//绑定手机号
        wechatPcLogin({
            mobile: data.mobile,
            validCode: data.code,
            code: state.CODE
        }).then(res => {
            commit('BIND_PHONE', res)
        }).catch(error => {
            console.log(error)
        })
    }

}

// 改变
let mutations = {
    SET_LOGIN_CAPTCHA(state, data) {
        state.uuid = new Date().getTime() + '' + data
        let base = process.env.BASE_URL
        let front = `${base}/web/api/`
        // if (process.env.NODE_ENV !== 'production') {
        //     front = `${base}/front`
        // } else {
        //     front = `${base}`
        // }
        state.captcha = `${front}/auth/captcha?uuid=` + state.uuid
    },
    SET_WECHAT_STATE(state, data) {
        state.CODE = data.data.code
        state.wechat_state = data.res
    },
    SET_CODE(state, data) {
        state.sendCode_state = data
    },
    BIND_PHONE(state, data) {
        state.bindPhone_state = data
    }
}
// 获取
let getters = {
    changeCaptcha(state) {
        return state.captcha
    },
    wechatState(state) {
        return state.wechat_state
    },
    codeState(state) {
        return state.sendCode_state
    },
    bindPhoneState(state) {
        return state.bindPhone_state
    }
}
export default {
    namespaced,
    actions,
    state,
    mutations,
    getters
}
