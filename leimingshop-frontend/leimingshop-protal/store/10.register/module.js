import { noticeProtocol } from '@/api/api_10register.js'
const namespaced = true
// 数据
let state = () => ({
    noticeProtocol: {
        data: {
            docContent: ''
        }
    },
    captcha: '',
    uuid: ''
})

// 事件
let actions = {
    getNoticeProtocol({ commit }) {
        noticeProtocol({
            type: 1
        }).then(res => {
            commit('SET_NOTICE_PROTOCOL', res)
        }).catch(error => {
            console.log(error)
        })
    },
    getCaptcha({ //获取图形验证码
        commit
    }) {
        let random = ''
        function getRandom(num) {
            random = Math.floor((Math.random() + Math.floor(Math.random() * 9 + 1)) * Math.pow(10, num - 1));
        }
        getRandom(10);
        commit('SET_REGISTER_CAPTCHA', random);
    },

}

// 改变
let mutations = {
    SET_NOTICE_PROTOCOL(state, data) {
        state.noticeProtocol = data
    },
    SET_UUID(state, data) {
        console.log(state, 'state', data)
        state.uuid = data
        console.log
    },
    SET_REGISTER_CAPTCHA(state, data) {
        state.uuid = new Date().getTime() + '' + data
        let base = process.env.BASE_URL
        state.captcha = base + '/web/api/member/register/generate/image?uuid=' + state.uuid
    }

}
// 获取
let getters = {
    aptchaData(state) {
        return `${state.captcha}`
    }
}
export default {
    namespaced,
    actions,
    state,
    mutations,
    getters
}
