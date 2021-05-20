import {
    CartPage,
    CartRecommend,
    AddCar,
    CheckAall,
    UpdateCheck,
    CartDeleteNologin,
    CartFavorites,
    Getcoupon,
    CartDelete1,
    CartDelete
} from '@/api/api_04.shoppingCar.js'

const namespaced = true
// 数据
let state = () => ({
    cartData: '', // 购物车数据
    allChecked: false, //是否全选
    recommendList: '', //购物车的商品推荐列表
    recommendTotalPage: '', //购物车的商品推荐列表总页数
    selArr: [],//批量删除的specId
    collectStatus: false,//收藏成功与否判断
    numDisabled: false,
})

// 事件
let actions = {
    // 购物车列表
    cartPage({ commit }) {
        CartPage().then(res => {
            if (res.data && res.data.findCartDTO) {
                res.data.findCartDTO.map((item, index) => {
                    item.isSelectAll = item.isSelectAll === 0 ? false : true;
                    item.activityCartGroupDTOList.map(item2 => {
                        item2.cartDTOList.map(item3 => {
                            item.storeType = item3.storeType == 1 ? '自营' : '';

                            if (item3.activityType != 3 && item3.activityType != 5) {
                                // 判断specStoreage
                                if (item3.specStorage < 200) {
                                    item3.endNumber = item3.specStorage;
                                } else {
                                    item3.endNumber = 200;
                                }
                            } else {
                                if (item3.restrictionQuantity == 0) {// 不限购，判断activitySurplusStorage
                                    if (item3.activitySurplusStorage > 200) {
                                        item3.endNumber = 200;
                                    } else {
                                        item3.endNumber = item3.activitySurplusStorage;
                                    }
                                } else {// 限购
                                    if (item3.restrictionQuantity > 200 && item3.activitySurplusStorage > 200) {
                                        item3.endNumber = 200;
                                    } else {
                                        if (item3.restrictionQuantity > 200 && item3.activitySurplusStorage < 200) {
                                            item3.endNumber = item3.activitySurplusStorage;
                                        }
                                        if (item3.restrictionQuantity < 200 && item3.activitySurplusStorage > 200) {

                                            item3.endNumber = item3.restrictionQuantity;
                                        }
                                        if (item3.restrictionQuantity < 200 && item3.activitySurplusStorage < 200) {
                                            if (item3.restrictionQuantity > item3.activitySurplusStorage) {
                                                item3.endNumber = item3.activitySurplusStorage;
                                            } else {
                                                item3.endNumber = item3.restrictionQuantity;
                                            }
                                        }
                                    }
                                }
                            }
                        })
                    })
                })
            }
            state.numDisabled = false;
            commit('setCartData', res.data)
            commit('setNumDisabled', false)
        })
    },
    // 商品推荐列表
    cartRecommend({ commit }, obj) {
        console.log('当前页数', obj.page)
        CartRecommend(obj).then(res => {
            commit('setRecommend', res.data)
            state.recommendTotalPage = Math.ceil(res.data.total / obj.limit);
            // console.log(state.recommendTotalPage,'购物车的商品推荐列表',state.recommendList)
        })
    },
    // 加入购物车
    addCar({ commit, dispatch }, obj) {
        console.log('=====', obj)
        commit('setNumDisabled', true)
        AddCar(obj).then(res => {
            console.log('加入结果', res)
            dispatch('cartPage')
                // commit( 'setNumDisabled', false )
                // commit( 'setNumDisabled', false )
        })
    },
    checkAall({ commit, dispatch }, obj) {
        console.log('当前', obj)
        CheckAall(obj).then(res => {
            console.log('改变结果', res)
            if (res.code == 200) {
                dispatch('cartPage')
            }
        })
    },
    checkItem({ commit, dispatch }, obj) {
        UpdateCheck(obj).then(res => {
            console.log('改变结果', res)
            if (res.code == 200) {
                dispatch('cartPage')
            }
        })
    },
    // 未登录删除缓存中的购物车
    nologinDelete({ commit, dispatch }, obj) {
        console.log('====', obj)
        CartDeleteNologin(obj).then(res => {
            console.log('删除结果', res)
            if (res.code == 200) {
                dispatch('cartPage')
            }
        })
    },
    // 已登录批量删除和单个删除购物车
    loginCartDeletes({ commit, dispatch }, obj) {
        console.log('==222222222222222==', obj)
        CartDelete(obj).then(res => {
            console.log('删除结果', res)
            if (res.code == 200) {
                dispatch('cartPage')
            }
        })
    },
    loginCartDeletes1({ commit, dispatch }, obj) {
        CartDelete1(obj).then(res => {
            console.log('删除结果', res)
            if (res.code == 200) {
                dispatch('cartPage')
            }
        })
    },
    // 收藏
    cartFavorites({ commit, dispatch }, obj) {
        console.log('收藏参数', obj)
        CartFavorites(obj).then(res => {
            console.log('收藏结果', res)
            if (res.code == 200) {
                commit('setCollectStatus', true)
                setTimeout(() => {
                    commit('setCollectStatus', false)
                }, 4000)
                dispatch('cartPage')
            }
        })
    },
    // 领取优惠券
    getcoupon({ commit, dispatch }, obj) {
        console.log('领取参数', obj)
        Getcoupon(obj).then(res => {
            console.log('领取结果', res)
            if (res.code == 200) {
                dispatch('cartPage')
            }
        })
    },

}

// 改变
let mutations = {
    setCartData(state, data) {
        console.log('=====', data)
        data.totalPrice = Number(parseFloat((data.totalPrice)).toFixed(2))
        state.cartData = data;
        state.selArr = [];
        state.allChecked = data.isSelectAll === 0 ? false : true;
        if (state.cartData && state.cartData.findCartDTO && state.cartData.findCartDTO.length != 0) {
            state.cartData.findCartDTO.map(v => {
                v.activityCartGroupDTOList.map(v => {
                    v.cartDTOList.map(i => {
                        if (i.isSelect == 1) {
                            state.selArr.push(i.specId);
                        }
                    });
                });
            });
        }
    },
    setRecommend(state, data) {
        state.recommendList = data.list;
        console.log('=======', state.recommendList)
    },
    setCollectStatus(state, data) {
        state.collectStatus = data;
    },
    setNumDisabled(state, data) {
        state.numDisabled = data;
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
