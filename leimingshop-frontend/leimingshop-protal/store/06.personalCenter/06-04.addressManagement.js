
import {
    getAddressList
} from '@/api/api_06personalCenter.js'

const namespaced = true

// 数据
let state = () => ({
    addressListObject: {},
    pageTotal: 50,
    pageFlag: false, //总页数请求成功，翻页再出现
    page: {
        currentPage: 1, //当前所在的页数，默认是第一页
        limit: 10 //每页显示多少条数据
    },
})

// 事件
let actions = {
    getAddressList({ commit, state }, params) {
        console.log(state.page.limit);
        console.log(params);
        getAddressList(params).then((res) => {
            commit("setAddressList", res)
        }).catch((err) => {
            console.log(err);
        })
    },
    //切换页数得到当前页码
    getCurrentPage({ commit }, val) {
        commit("setCurrentPage", val)
    },
    //删除某条数据 判断当前页是否需要减去1
    getAnswer({ commit }) {
        commit("setAnswer")
    }
}

// 改变
let mutations = {
    setAddressList(state, params) {
        state.addressListObject = params.data;
        state.pageTotal = params.data.total;
        if (state.pageTotal > 0) {
            state.pageFlag = true
        }
    },
    //改变当前页码
    setCurrentPage(state, val) {
        state.page.currentPage = val;
    },
    //判断当前页是否需要减去一
    setAnswer(state) {
        //    let actualPage=Math.ceil(state.pageTotal/state.page.limit);
        //    console.log(actualPage);
        console.log(state.pageTotal);
        console.log((state.page.currentPage - 1) * state.page.limit + 1);
        if (state.pageTotal == (state.page.currentPage - 1) * state.page.limit + 1) {
            state.page.currentPage--
        }
        console.log(state.page.currentPage);
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
