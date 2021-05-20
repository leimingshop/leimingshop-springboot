
import {
    getPointsDetails, getMyPointsDetail, getMyPointsWeek
} from '@/api/api_06personalCenter.js'
const namespaced = true
// 数据
let state = () => ({
    page: {
        currentPage: 1, //当前页数
        limit: 10 //每页条数
    },
    pageSize: 5,
    total: 30, //一共多少条
    detailedList: [], //数据列表
    MyPointsDetailList: {}, //我的积分列表
    MyPointsOneWeek: [],//最近一周的积分列表
})

// 事件
let actions = {
    getMyPointsDetailData({ commit }) {
        getMyPointsDetail().then((res) => {
            console.log(res.data);
            commit("setMyPointsDetailData", res.data.data)
        }).catch((err) => {
            console.log(err);
        })
    },
    getPointsDetailsData({ commit }, params) {
        getPointsDetails(params).then((res) => {
            commit('setPointsDetailsData', res.data.data)
        }).catch((err) => {

        })
    },
    getMyPointsWeekData({ commit }) {
        getMyPointsWeek().then((res) => {
            commit("setMyPointsWeekData", res.data)
        }).catch((err) => {
            console.log(err);
        })
    }
}

// 改变
let mutations = {
    setPointsDetailsData(state, params) {
        let data = params;
        state.total = data.total;
        state.detailedList = data.list;
        let currentDate = state.detailedList[0].createDate;
        let currentDate1 = currentDate.split(" ")[0].split("-");
        state.currentDate = `${currentDate1[0]}年${currentDate1[1]}月`;
        state.currentValue = state.detailedList[0].currentValue;
    },
    setMyPointsDetailData(state, params) {
        console.log(params);
        state.MyPointsDetailList = params;
    },
    setMyPointsWeekData(state, params) {
        state.MyPointsOneWeek = params.data;
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
