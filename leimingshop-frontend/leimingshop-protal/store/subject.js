
import {
        topicInfoApi,
} from '@/api/api_01main'

const namespaced = true

// 数据
let state = () => ({
        // hasSearchList: true, //首次加载 是否有搜索结果列表
        subjectLoading: true, // 列表页加载中
        subjectGoodsList: [], // 商品列表
        subjectTotal: 0,
        subjectImg: '',
        subjectParam: {
                id: '',
                limit: 20,
                page: 1,
        },
})

// 事件
let actions = {
        // 获取专题数据
        async getSubjectData({ commit, state }, params) {
                state.subjectLoading = true
                const res = await topicInfoApi(state.subjectParam)
                state.subjectLoading = false
                if (res && res.code == 200) {
                        commit('HANDLE_SUBJECT_GOODS', res.data)
                }
        },
        // 切换分页
        async changePage({ commit, state, dispatch }, page) {
                commit('HANDLE_CHANGE_PAGENO', page);
                dispatch('subject/getSubjectData')
        },
}

// 改变
let mutations = {
        // 同步分页
        HANDLE_CHANGE_PAGENO(state, data) {
                state.subjectParam.page = parseInt(data)
        },
        // 同步专题数据
        HANDLE_SUBJECT_GOODS(state, data) {
                console.log(data, '商品搜索列表')
                state.subjectGoodsList = data.goodsDTOList
                state.subjectTotal = data.total
                state.subjectImg = data.topicPicturePc.split(',')
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
