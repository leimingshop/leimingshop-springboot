import {
    getNoevaluatedList,
    getEvaluatedList,
    // getLookEvaluated
} from '@/api/api_06-02.evaluationSheet.js'
const namespaced = true

// 数据
let state = () => ({
    // 定义待评价数据
    evaluateListObject: {
        list: [],
        total: 0,
        page: 1,    //当前页码
        limit: 50   //每页显示记录数
    },
    // 定义已评价数据
    evaluateListObj: {
        list: [],
        total: 0,
        page: 1,    //当前页码
        limit: 5   //每页显示记录数
    },
    // 查看评价
    // lookEvaluated:{},
    pageTotal: 50
})

// 事件
let actions = {
    // 获取未评价商品列表数据
    getNoevaluatedList({ commit }, params) {
        return new Promise((resolve) => {
            getNoevaluatedList(params).then(res => {
                commit('setNoevaluatedList', res)
                resolve("");
            }).catch(err => {
                console.log(err);
                resolve("");
            })
        })
    },
    // 获取已评价商品列表数据
    getEvaluatedList({ commit }, params) {
        return new Promise((resolve) => {
            getEvaluatedList(params).then(res => {
                commit('setEvaluatedList', res)
                resolve("");
            }).catch(err => {
                console.log(err);
                resolve("");
            })
        })
    },

    // getLookEvaluated({commit}, params){
    //     getLookEvaluated(params).then(res=> {
    //         console.log(111,res);
    //         commit('setLookEvaluated',res)
    //     }).catch(err=>{
    //         console.log(err);
    //     })
    // }
}

// 改变
let mutations = {
    // 修改待评价方法
    setNoevaluatedList(state, params) {
        // params.data = {
        //     list:[],
        //     total: 0
        // },
        state.evaluateListObject = params.data;

        state.pageTotal = params.data.total;
    },
    setEvaluatedList(state, params) {
        // params.data = {
        //     list:[],
        //     total: 0
        // },
        state.evaluateListObj = params.data;
        state.pageTotal = params.data.total;
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












