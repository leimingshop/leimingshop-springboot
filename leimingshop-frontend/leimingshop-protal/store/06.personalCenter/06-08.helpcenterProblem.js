// import {
//     getMyFirstClassList,
//     getMyChildClassList,
//     getMyHelpList
// } from '@/api/api_06-14.helpCenter.js'

// const namespaced = true

// // 数据
// let state = {
//     MyFirstClassList:[{}],   //一级分类
//     MyChildClassList:[{}],    //子分类集合
//     allId: [],
//     MyHelpList: []
// }

// // 事件
// let actions = {
//     // 帮助中心首页
//     getMyFirstClassListData({ commit,dispatch }, params) {
//     // getMyFirstClassListData({ commit }, params) {
//         getMyFirstClassList(params).then((res) => {
//             if (res.code == 200) {
//                 commit("setMyFirstClassListData", res.data)
//                 for (var i = 0; i < res.data.length; i++) {
//                     dispatch('getMyChildClassListData', res.data[i].id)
//                 }
//             }
//         }).catch((err) => {
//             console.log(err)
//         })
//     },
//     // 帮助中心左侧菜单
//     getMyChildClassListData({ commit }, params) {
//         getMyChildClassList({
//             parentId:params
//         }).then((res) => {
//             console.log(res)
//             if (res.code == 200) {
//                 commit("setMyChildClassListData", {resData:res.data,parentId:params})
//             }
//         }).catch((err) => {
//             console.log(err);
//         })
//     }

// }

// // 改变
// let mutations = {
//     // 帮助中心首页
//     setMyFirstClassListData(state, params) {
//         state.MyFirstClassList = params;
//     },
//     // 帮助中心左侧菜单
//     setMyChildClassListData(state, params) {
//         // state.MyChildClassList = params
//         state.MyChildClassList = params.resData;

//         for (var i = 0; i < state.MyFirstClassList.length; i++) {
//             if (params.parentId == state.MyFirstClassList[i].id) {
//                 state.MyFirstClassList[i].children = params.resData
//                 // state.MyFirstClassList[i].children[0].id
//             }
//         }
//     }

// }
// // 获取
// let getters = {
//     getFirstClass(state){
//         return state.MyChildClassList
//     }
// }
// export default {
//     namespaced,
//     actions,
//     state,
//     mutations,
//     getters
// }