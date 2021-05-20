import {
    classifyList
} from '@/api/api_03.goodsClass'


const namespaced = true

// 数据 测试id（specId: 1197354647233236993，goodsId: 1197354647237431297 ）
let state = () => ({
    breadcrumbList: [], // 面包屑
    goodsClassVOList: [], // 商品分类信息
})

// 事件
let actions = {
    // 分类查询列表以及面包屑
    async handleClassifyList({ commit, state }, params) {
        const res = await classifyList(params)

        if (res && res.code == 200) {
            commit('HANDLE_CLASSIFY_LIST', res.data)
        }
    },
}

// 改变
let mutations = {
    // 根据id查询子类数据
    HANDLE_CLASSIFY_LIST(state, data) {
        let tempList = [
            {
                title: '首页',
                toPath: '/'
            }
        ];

        (function breadcrumb(obj) {
            if (!obj) return

            let temp = {
                title: obj.gcName,
                toPath: `/proClassification?classId=${obj.id}&dataSource=currentPage`,
                classId: obj.id
            }

            tempList.push(temp)

            if (obj.children) {
                breadcrumb(obj.children)
            } else {
                tempList[tempList.length - 1].toPath = ''
                return
            }

        })(data.currentClassVo)

        state.breadcrumbList = tempList
        state.goodsClassVOList = data.goodsClassVos
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

