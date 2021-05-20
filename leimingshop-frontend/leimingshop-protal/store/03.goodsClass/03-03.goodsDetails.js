// const Router = $nuxt.$router
import Cookies from 'js-cookie'
import {
    goodsDetails,
    goodsDetailsCoupon,
    goodsReduceActivityList,
    shopGoodsHot,
    shopGoodsLike,
    goodsCollection,
    goodsCancelCollection,
    goodsFavNum,
    goodsIsCollected,
    storeCollection,
    storeCancelCollection,
    addToShoppingCart,
    cartBuyNow,
    storeInfo,
    goodsCommentPage,
    browseRecord,
    couponReceive
} from '@/api/api_03.goodsClass'

const namespaced = true

// 数据 测试id（specId: 1197354647233236993，goodsId: 1197354647237431297 ）
let state = () => ({
    loginFlag: 0, // 是否登录 0 未登录 1已登录
    goodsDetails: null, // 商品详情
    breadcrumbList: [], // 面包屑
    reduceList: null, // 满减列表
    couponList: null, // 优惠券列表
    shopInfo: null, // 店铺详情
    shopIsCollect: 0, //店铺是否收藏 1:已收藏，0:未收藏
    goodsSpecNameValue: null, // 商品规格属性名集合
    goodsSpecList: null, // 商品规格属性和属性值对应关系
    goodsCommentList: null, // 商品评论列表
    goodsIsCollect: 0, // 商品是否收藏 1:已收藏，0:未收藏
    goodsFavNum: 0, // 商品收藏数量
    commentTotalCount: 0, // 评论列表总条数
    hasComment: false, // 该商品是否有评论
    commentAboutNum: null, // 评论相关数量
    goodsHotList: null, // 店铺热卖 - 商品列表
    goodsLikeList: null, // 同类推荐 - 商品列表
    detailsLoading: true,
    couponLoading: false,
    couponReceiveLoading: false,
    goodsHotLoading: true,
    goodsLikeLoading: false,
    goodsCollectLoading: false,
    storeCollectLoading: false,
    addCartLoading: false,
    cartBuyNowLoading: false,
    goodsCommentLoading: false,
    shopInfoLoading: true,
    isGoodsDetailsReload: true, //数据异常，重新刷新商品详情开关
})

// 事件
let actions = {
    handleGoodsDetails({ dispatch, commit, state }, params) {
        return new Promise(async (resolve, reject) => {
            state.detailsLoading = true
            const res = await goodsDetails(params)
            state.detailsLoading = false
            if (res) {
                resolve(true)

                if (res.code == 200) {
                    // 商品详情
                    commit('HANDLE_GOODS_DETAILS', { data: res.data, params })

                } else {
                    // 只用specId去调用接口的话如果不返回200 ，可能规格已经不存在，这时用goodsId去调用接口
                    // 去查询当前商品的其他商品信息
                    // 测试链接 http://localhost:8080/#/goodsDetails?goodsId=1262562329103458306&specId=1262562329074098177
                    commit('HANDLE_GOODS_DETAILS_RELOAD')
                }
            } else {
                resolve(false)
            }
        })
    },

    // 商品可选满减活动列表
    async handleGoodsReduceActivityList({ commit, state }, params) {
        const res = await goodsReduceActivityList(params)

        if (res && res.code == 200) {
            commit('HANDLE_GOODS_REDUCE_ACTIVITY_LIST', res.data)
        }
    },

    async handleGoodsDetailsCoupon({ commit, state }, params) {
        state.couponLoading = true
        const res = await goodsDetailsCoupon(params)
        state.couponLoading = false

        if (res && res.code == 200) {
            commit('HANDLE_GOODS_DETAILS_COUPON', res.data)
        }
    },

    async handleShopInfo({ commit, state }, params) {
        state.shopInfoLoading = true
        const res = await storeInfo(params)
        state.shopInfoLoading = false

        if (res && res.code == 200) {
            commit('HANDLE_SHOP_INFO', res.data)
            commit('HANDLE_STORE_COLLECTION', res.data.isFavorite)
        }
    },

    async handleShopGoodsHot({ commit, state }, params) {
        state.goodsHotLoading = true
        const res = await shopGoodsHot(params)
        state.goodsHotLoading = false

        if (res && res.code == 200) {
            commit('HANDLE_SHOP_GOODS_HOT', res.data)
        }
    },

    async handleGoodsLike({ commit, state }, params) {
        state.goodsLikeLoading = true
        const res = await shopGoodsLike(params)
        state.goodsLikeLoading = false

        if (res && res.code == 200) {
            commit('HANDLE_GOODS_LIKE', res.data)
        }
    },

    async handleGoodsCollection({ commit, state }, params) {
        state.goodsCollectLoading = true
        const res = await goodsCollection(params)
        state.goodsCollectLoading = false

        if (res && res.code == 200) {
            commit('HANDLE_GOODS_COLLECTION', 1);
            this._vm.$Message.success(res.msg);
            state.goodsFavNum += 1;
        }
    },

    async handleGoodsCancelCollection({ commit, state }, params) {
        state.goodsCollectLoading = true
        const res = await goodsCancelCollection(params)
        state.goodsCollectLoading = false

        if (res && res.code == 200) {
            commit('HANDLE_GOODS_COLLECTION', 0);
            this._vm.$Message.success(res.msg);
            state.goodsFavNum -= 1;
        }
    },

    async handleGoodsFavNum({ commit, state }, params) {
        const res = await goodsFavNum(params)

        if (res && res.code == 200) {
            commit('HANDLE_GOODS_FAV_NUM', res.data || 0)
        }
    },

    async handleGoodsIsCollected({ commit, state }, params) {
        const res = await goodsIsCollected(params)

        if (res && res.code == 200) {
            commit('HANDLE_GOODS_IS_COLLECTED', res.data)
        }
    },

    async handleStoreCollection({ commit, state }, params) {
        state.storeCollectLoading = true
        const res = await storeCollection(params)
        state.storeCollectLoading = false

        if (res && res.code == 200) {
            commit('HANDLE_STORE_COLLECTION', 1)
            this._vm.$Message.success(res.msg);
        }
    },

    async handleStoreCancelCollection({ commit, state }, params) {
        state.storeCollectLoading = true
        const res = await storeCancelCollection(params)
        state.storeCollectLoading = false

        if (res && res.code == 200) {
            commit('HANDLE_STORE_COLLECTION', 0)
            this._vm.$Message.success(res.msg);
        }
    },

    async handleAddToShoppingCart({ commit, state, dispatch }, params) {
        state.addCartLoading = true
        const res = await addToShoppingCart(params)
        state.addCartLoading = false

        if (res && res.code == 200) {
            Cookies.set('rediskey', res.data||'')
            this._vm.$Message.success(res.msg);
            this.dispatch('main/actCartList')
        }
    },

    handleCartBuyNow({ commit, state }, params) {
        return new Promise(async (resolve, reject) => {
            state.cartBuyNowLoading = true
            const res = await cartBuyNow(params)
            state.cartBuyNowLoading = false

            if (res && res.code == 200) {
                resolve(true)
            } else {
                resolve(false)
            }
        })

    },

    async handleGoodsComment({ commit, state }, params) {
        console.log(params.goodsId)
        if(!params.goodsId) {
            return
        }
        state.goodsCommentLoading = true
        const res = await goodsCommentPage(params)
        state.goodsCommentLoading = false

        if (res && res.code == 200) {
            commit('HANDLE_GOODS_COMMENT', { data: res.data, params })
        }
    },

    // 浏览记录 埋点
    handleBrowseRecord({ commit, state }, params) {
        browseRecord(params)
    },

    // 优惠券领取
    async handleCouponReceive({ commit, state }, params) {
        const {couponId, checkIndex} = params
        state.couponReceiveLoading = true
        const res = await couponReceive(couponId)
        state.couponReceiveLoading = false
        if (res && res.code == 200) {
            this._vm.$Message.success(res.msg)
            commit('HANDLE_COUPON_RECEIVE', couponId)
        } else if (res&&res.code==505&&res.errorCode==="403103"){
            commit('HANDLE_COUPON_OVERDUE', checkIndex)
        }
    }
}

// 改变
let mutations = {
    HANDLE_COUPON_OVERDUE(state, index) {
        console.log(index)
        state.couponList[index].overdueFlag = true
    },
    HANDLE_RESET_DETAILS(state) {
        state.goodsDetails = null
        state.couponList = null
        state.shopInfo = null
    },

    HANDLE_GOODS_DETAILS(state, { data, params }) {

        let goodsDetailsVO = data.goodsDetailsVO

        // ps ： 以下所有活动列表所需的goodsId，都从商品详情接口获取，防止路由参数异常，造成数据获取异常

        /*
         *后台管理端进入 详情页，没有goodsId，手动获取
         *or
         *规格id不存在，重新获取
        */
        if (
            ! this._vm.$nuxt._router.currentRoute.query.goodsId
            || params.specId != goodsDetailsVO.id
        ) {
             this._vm.$nuxt._router.replace({
                name: 'goodsDetails',
                query: {
                    goodsId: goodsDetailsVO.goodsId,
                    specId: goodsDetailsVO.id
                }
            })
        }

        // 商品详情
        state.goodsDetails = goodsDetailsVO

        // 是否登录
        state.loginFlag = data.loginFlag

        // 面包屑
        this.commit('goodsDetails/HANDLE_BREADCRUMB', goodsDetailsVO)

        // 规格列表
        state.goodsSpecList = data.goodsSpecList

        // 商品收藏数量
        this.dispatch('goodsDetails/handleGoodsFavNum', { goodsId: goodsDetailsVO.goodsId })

        // 商品是否被收藏
        this.dispatch('goodsDetails/handleGoodsIsCollected',
            {
                storeId: goodsDetailsVO.goodsVO.storeId,
                specId: goodsDetailsVO.id
            }
        )

        // 商品规格
        this.commit('goodsDetails/HANDLE_GOODS_SPEC', data.specAttrMap)

        /*
           * 优惠券
           * 测试链接 http://localhost:8080/#/goodsDetails?goodsId=1254253703137861634&specId=1254253703137861633
        */
        if (goodsDetailsVO.couponsFlag == 1) {
            this.dispatch('goodsDetails/handleGoodsDetailsCoupon',
                {
                    goodsId: goodsDetailsVO.goodsId
                }
            )
        }

        // 满减
        if (goodsDetailsVO.reduceFlag == 1) {
            this.dispatch('goodsDetails/handleGoodsReduceActivityList',
                {
                    goodsId: goodsDetailsVO.goodsId
                }
            )
        }

        // 活动列表（现包括秒杀，将来新增活动，可扩展）
        this.commit('goodsDetails/HANDLE_GOODS_ACTIVITY_LIST', goodsDetailsVO.specActivityList)

        // 店铺详情
        this.dispatch('goodsDetails/handleShopInfo', goodsDetailsVO.goodsVO.storeId)
    },

    HANDLE_GOODS_REDUCE_ACTIVITY_LIST(state, data) {
        state.reduceList = data
    },

    HANDLE_GOODS_DETAILS_RELOAD(state) {
        if (!state.isGoodsDetailsReload) return

        state.isGoodsDetailsReload = false

        let params = {
            goodsId: $nuxt.$router.currentRoute.query.goodsId,
            specId: $nuxt.$router.currentRoute.query.specId,
        }

        this.dispatch('goodsDetails/handleGoodsDetails', params)
    },

    HANDLE_GOODS_DETAILS_COUPON(state, data) {
        // 可使用优惠券列表
        let canRecList = data.canRecList
        canRecList.forEach((item, index) => {
            item.isReceived = 0
            item.overdueFlag = false
        })

        // 已领取优惠券列表
        let recedList = data.recedList.filter((item, index) => {
            item.isReceived = 1
            item.overdueFlag = false
            return item.couponsState == 1
        })

        let tempList = [].concat(canRecList).concat(recedList)
        state.couponList = tempList
    },

    HANDLE_COUPON_RECEIVE(state, data) {
        let tempList = state.couponList
        for (let item of tempList) {
            if (item.id == data) {
                item.isReceived = 1;
                break;
            }
        }
        state.couponList = tempList
    },
    HANDLE_GOODS_ACTIVITY_LIST(state, data) {
        if (data) {
            data.forEach((item, index) => {
                if (item.activityType == 3) {
                    state.goodsDetails.frontSeckillActivityPageDTO = item
                    state.goodsDetails.seckillFlag = 1
                } else if (item.activityType == 5){
                    state.goodsDetails.frontSeckillActivityPageDTO = item
                    state.goodsDetails.seckillFlag = 2
                } else {
                     state.goodsDetails.seckillFlag = 0
                }
            })
        }
    },

    HANDLE_BREADCRUMB(state, data) {
        let goodsVO = data.goodsVO
        let breadcrumbList = [
            {
                title: '首页',
                toPath: '/'
            }
        ]

        if (goodsVO.firstGcId) {
            breadcrumbList.push({
                title: goodsVO.firstGcName,
                classId: goodsVO.firstGcId,
                toPath: `/proClassification?classId=${goodsVO.firstGcId}`
            })
        }

        if (goodsVO.secondGcId) {
            breadcrumbList.push({
                title: goodsVO.secondGcName,
                classId: goodsVO.secondGcId,
                toPath: `/proClassification?classId=${goodsVO.secondGcId}`
            })
        }

        if (goodsVO.gcId) {
            breadcrumbList.push({
                title: goodsVO.gcName,
                classId: goodsVO.gcId,
                toPath: `/proClassification?classId=${data.goodsVO.gcId}`
            })
        }

        if (data.specName) {
            breadcrumbList.push({
                title: data.specName.length < 8 ? data.specName : `${data.specName.slice(0, 8)}...`,
                fullTitle: data.specName,
                toPath: ''
            })
        }

        state.breadcrumbList = breadcrumbList
    },

    HANDLE_GOODS_SPEC(state, data) {
        let tempArr = []
        for (var item in data) {
            tempArr.push(data[item])
        }
        state.goodsSpecNameValue = tempArr
    },

    HANDLE_SHOP_GOODS_HOT(state, data) {
        state.goodsHotList = data
    },

    HANDLE_GOODS_LIKE(state, data) {
        state.goodsLikeList = data
    },

    HANDLE_SHOP_INFO(state, data) {
        state.shopInfo = data
    },

    HANDLE_GOODS_COLLECTION(state, data) {
        state.goodsIsCollect = data
    },

    HANDLE_GOODS_FAV_NUM(state, data) {
        state.goodsFavNum = data
    },

    HANDLE_GOODS_IS_COLLECTED(state, data) {
        // 未登录查询时 会返回 data = null
        state.goodsIsCollect = data ? data.goodsFav : 0
    },

    HANDLE_STORE_COLLECTION(state, data) {
        state.shopIsCollect = data
    },

    HANDLE_GOODS_COMMENT(state, { data, params }) {
        if (!data) return

        let commentAboutNum = {}

        for (var i in data) {
            if (typeof data[i] != "object") {
                commentAboutNum[i] = data[i]
            }
        }
        state.commentAboutNum = commentAboutNum

        if (!data.page) return
        state.goodsCommentList = data.page.list

        // 记录当前商品的评论总数
        if (state.hasComment == false) {
            state.commentTotalCount = data.page.total
            state.hasComment = true
        }
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
