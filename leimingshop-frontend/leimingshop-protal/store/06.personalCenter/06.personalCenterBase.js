const namespaced = true

// 数据
let state = () => ({
    index1Flag: 0,
    index2Flag: 0,
    personCenterTitle: "个人中心",
    barList: []
})

// 事件
let actions = {

}

// 改变
let mutations = {
    SET_DISPLAY_STYLE(state, data) {
        state.index1Flag = data.index1Flag
        state.index2Flag = data.index2Flag
        state.personCenterTitle = data.personCenterTitle
        state.barList = data.barList
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












