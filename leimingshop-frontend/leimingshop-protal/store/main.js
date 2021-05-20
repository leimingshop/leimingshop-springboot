import {
    IndexAdv, //轮播图
    IndexTips, //搜索智能匹配接口
    IndexFloor, //楼层
    IndexCustomClass, //首页商品分类
    IndexRecommendPage, //推荐
    IndexHotWord, //热搜词
    IndexSite, //站点信息
    ActivityCouponCenter, //优惠券
    ActivityCouponReceive, //领取优惠券
    ActivitySeckillHome, //首页秒杀中心
    ActivitySeckillAreaTime, //秒杀专区-时间段
    ActivitySeckillCurrentGoods, //秒杀专区-正在秒杀商品列表
    ActivitySeckillSoonGoods, //秒杀专区-即将秒杀商品列表   没有用到这个字段   用的上面的
    CartList, //首页购物车
    DeleteCart, //首页购物车删除
    LoginCartDelete, //登录后首页购物车删除
    StoreInfoStoreId, //店铺首页顶部
    StoreRecommend, //店铺推荐专区
    GoodsStoreClassAll, //店铺商品分类
    messageCount,//获取未读消息数量
    ActivitySeckillRemind, //秒杀专区-设置提醒
    IndexNav // 导航栏


} from '~/api/api_01main.js'

const namespaced = true
// 数据
let state = () => ({
    IndexAdvData: [], //轮播图
    curFloor: 1, //pc首页值为：1 店铺首页：店铺Id
    IndexFloorData: [], //楼层
    IndexCustomClassData: [], //首页商品分类
    IndexRecommendPageData: [], //推荐
    IndexHotWordData: [], //热搜词
    IndexSiteData: {}, //站点信息
    ActivityCouponCenterData: [], //领优惠券
    ActivitySeckillHomeData: [], //首页秒杀中心
    ActivitySeckillRefreshCount: 0, //首页秒杀中心已刷新次数
    activitySeckillAreaTimeData: [], //秒杀专区-时间段
    ActivitySeckillCurrentGoodsData: [], //秒杀专区-正在秒杀商品列表
    ActivitySeckillSoonGoodsData: [], //秒杀专区-即将秒杀商品列表
    CartListData: [], //首页购物车
    StoreInfoStoreIdData: {}, //店铺首页顶部
    StoreRecommendData: [], //店铺推荐专区
    GoodsStoreClassAllData: [], //店铺商品分类
    IndexSearchkeyword: '',
    messageCountData: 0,
    page: 1, //推荐页数
    limit: 25,
    total: 0, //首页推荐总数
    iscompleted: false, // 下拉加载还有没有数据
    timeCount: {
        hours: 0,
        minutes: 0,
        seconds: 0
    },
    actIndexFloorData: [],
    IndexNavList: []
})

// 事件
let actions = {
    async actIndexNavList({ state, commit }, params) {
        const res = await IndexNav()
        commit('change_IndexNavList', res.data)

    },
    actIndexAdv({
        state,
        commit,
        dispatch
    }, val) { //轮播图
        IndexAdv(val).then(res => {
            if (!(res && res.code == 200)) return;
            state.IndexAdvData = [];
            state.IndexAdvData = res.data;
        }).catch(error => {

            console.log(error)
        })
    },
    actIndexTips({
        state,
        commit,
        dispatch
    }, callback) { //搜索智能匹配接口
        IndexTips({
            keyword: state.IndexSearchkeyword
        }).then(res => {
            if (!(res && res.code == 200)) return;
            callback(res.data)
        }).catch(error => {

            console.log(error)
        })
    },
    async actIndexFloor({
        state,
        commit,
        dispatch
    }, val) { //楼层
        try {
            const res = await IndexFloor(val)
            if (!(res && res.code == 200)) return;

            var IndexFloorData = res.data;
            for (var i = 0; i < IndexFloorData.length; i++) {
                IndexFloorData[i].linkType = IndexFloorData[i].actionType
                IndexFloorData[i].typeKeyWord = IndexFloorData[i].actionParams
                if (IndexFloorData[i].goodsDTOS && IndexFloorData[i].goodsDTOS.length) {
                    for (var j = 0; j < IndexFloorData[i].goodsDTOS.length; j++) {
                        var a = IndexFloorData[i].goodsDTOS[j].specSellPrice;
                        var specIntegerPrize = a.substring(0, a.indexOf('.'))
                        var specFloatPrize = a.substring(a.indexOf('.'))
                        IndexFloorData[i].goodsDTOS[j].specIntegerPrize = specIntegerPrize
                        IndexFloorData[i].goodsDTOS[j].specFloatPrize = specFloatPrize
                    }
                }
            }
            for (var i = 0; i < IndexFloorData.length; i++) {
                IndexFloorData[i].floorStyleName =
                    "enjoy-good-things" + IndexFloorData[i].floorStyle;
                IndexFloorData[i].linkType = IndexFloorData[
                    i
                ].actionType;
                IndexFloorData[i].typeKeyWord = IndexFloorData[
                    i
                ].actionParams;
                for (
                    var j = 0;
                    j < IndexFloorData[i].webFloorLinkConfigDTOList.length;
                    j++
                ) {
                    if (
                        IndexFloorData[i].webFloorLinkConfigDTOList[j]
                            .relationType &&
                        IndexFloorData[i].webFloorLinkConfigDTOList[j]
                            .relationTarget
                    ) {
                        IndexFloorData[i].webFloorLinkConfigDTOList[
                            j
                        ].linkType = IndexFloorData[
                            i
                        ].webFloorLinkConfigDTOList[j].relationType;
                        IndexFloorData[i].webFloorLinkConfigDTOList[
                            j
                        ].typeKeyWord = IndexFloorData[
                            i
                        ].webFloorLinkConfigDTOList[j].relationTarget;
                    }
                    if (
                        IndexFloorData[i].webFloorLinkConfigDTOList[j]
                            .typeKeyWord !== null
                    ) {
                        if (
                            IndexFloorData[i].webFloorLinkConfigDTOList[
                                j
                            ].typeKeyWord.indexOf(",") !== -1
                        ) {
                            var a = IndexFloorData[i].webFloorLinkConfigDTOList[
                                j
                            ].typeKeyWord.split(",")[2];
                            var specIntegerPrize = a.substring(0, a.indexOf("."));
                            var specFloatPrize = a.substring(a.indexOf("."));
                            IndexFloorData[i].webFloorLinkConfigDTOList[
                                j
                            ].specIntegerPrize = specIntegerPrize;
                            IndexFloorData[i].webFloorLinkConfigDTOList[
                                j
                            ].specFloatPrize = specFloatPrize;
                        }
                    }
                }
            }
            // state.IndexFloorData = IndexFloorData;
            commit('change_IndexFloorData', IndexFloorData);
            Promise.resolve(res)
            // then(res => {
            //     if (!(res && res.code == 200)) return;

            //     var IndexFloorData = res.data;
            //     for (var i = 0; i < IndexFloorData.length; i++) {
            //         IndexFloorData[i].linkType = IndexFloorData[i].actionType
            //         IndexFloorData[i].typeKeyWord = IndexFloorData[i].actionParams
            //         for (var j = 0; j < IndexFloorData[i].goodsDTOS.length; j++) {
            //             var a = IndexFloorData[i].goodsDTOS[j].specSellPrice;
            //             var specIntegerPrize = a.substring(0, a.indexOf('.'))
            //             var specFloatPrize = a.substring(a.indexOf('.'))
            //             IndexFloorData[i].goodsDTOS[j].specIntegerPrize = specIntegerPrize
            //             IndexFloorData[i].goodsDTOS[j].specFloatPrize = specFloatPrize
            //         }
            //     }
            //     state.IndexFloorData = IndexFloorData;
            //     console.log(state.IndexFloorData)
            // })
        }
        catch (error) {
            console.log('内部提交有异常', error)
        }
    },
    actGetMessageCount({
        state,
        commit,
        dispatch
    }) {
        //未读消息的数量
        // getNotifyCount() {
        messageCount().then(res => {
            if (res.code == 200) {
                state.messageCountData = res.data;
            }
        });
        //   },
    },
    actCartList({
        state,
        commit,
        dispatch
    }) { //首页购物车
        CartList().then(res => {
            if (!(res && res.code == 200)) return;
            commit('change_CartListData', res.data);
        }).catch(error => {

            console.log(error)
        })
    },
    async actIndexRecommendPage({
        state,
        commit,
        dispatch
    }, page) { //推荐
        try {
            let obj = {
                page: page ? page : state.page,
                limit: state.limit,
            }
            if (obj.page == 1) {
                state.IndexRecommendPageData = [];
            }
            // if ((page ? page : 1) * 25 >= state.total) state.iscompleted = true; //没有更多数据了

            const res = await IndexRecommendPage(obj)
            // .then(res => {
            if (res.code == 200) {
                state.total = res.data.total;
                state.page++
                if ((page ? page : 1) * 25 >= state.total) {
                    state.iscompleted = true
                }; //没有更多数据了
                state.IndexRecommendPageData = state.IndexRecommendPageData.concat(res.data.list);
                var IndexRecommendData = state.IndexRecommendPageData
                for (var i = 0; i < IndexRecommendData.length; i++) {
                    var a = IndexRecommendData[i].specSellPrice;
                    var specIntegerPrize = a.substring(0, a.indexOf('.'))
                    var specFloatPrize = a.substring(a.indexOf('.'))
                    IndexRecommendData[i].specIntegerPrize = specIntegerPrize
                    IndexRecommendData[i].specFloatPrize = specFloatPrize
                }
                state.IndexRecommendPageData = IndexRecommendData;
            }
        }
        catch (error) {

            console.log(error)
        }
    },
    actIndexHotWord({
        state,
        commit,
        dispatch
    }) { //热搜词
        IndexHotWord().then(res => {
            if (!(res && res.code == 200)) return;
            state.IndexHotWordData = res.data;
        }).catch(error => {
            console.log(error)
        })
    },
    actIndexSite({
        state,
        commit,
        dispatch
    }) { //站点信息
        IndexSite().then(res => {
            if (!(res && res.code == 200)) return;
            commit('changeIndexSiteData', res.data)
            // sessionStorage.setItem('WebsiteName', state.IndexSiteData.name)
        }).catch(error => {

            console.log(error)
        })
    },
    async activityCouponCenter({
        state,
        commit,
        dispatch
    }, page) { //优惠券
        let obj = {
            page: page ? page : state.page,
            limit: state.limit,
        }
        const res = await ActivityCouponCenter(obj)

        if (!(res && res.code == 200)) return;
        // state.total = res.data.total;
        // state.ActivityCouponCenterData = state.ActivityCouponCenterData.concat(res.data.list);
        state.ActivityCouponCenterData = res.data;


        state.ActivityCouponCenterData.forEach((item, index) => {
            state.ActivityCouponCenterData[index].isReceived = false;
        })
    },
    activityCouponReceive({
        state,
        commit,
        dispatch
    }, obj) { //领取优惠券
        ActivityCouponReceive({
            id: obj.id
        }).then(res => {
            if (!(res && res.code == 200)) return;
            commit('change_Coupon', obj.index)
        }).catch(error => {

            console.log(error)
        })
    },
    async actIndexCustomClass({
        state,
        commit,
        dispatch
    }) { //首页商品分类
        try {
            const res = await IndexCustomClass()
            if (!(res && res.code == 200)) { return };
            // state.IndexCustomClassData = res.data;
            // for (var i = 0; i < state.IndexCustomClassData.length; i++) {
            //     state.IndexCustomClassData[i].advLinkConfigDTOList = res.data[i].advDTO.advLinkConfigDTOList;
            // }
            // console.log(res)
            commit('change_IndexCustomClassData', res.data)
            return Promise.resolve(res.data)

            // }).catch(error => {

            //     console.log(error)
            // })
        } catch {

        }
    },
    actActivitySeckillHome({
        state,
        commit,
        dispatch
    }) { //首页秒杀中心
        ActivitySeckillHome().then(res => {
            if (!(res && res.code == 200)) return;
            var stateActivitySeckillHomeData = res.data;

            for (var i = 0; i < res.data.seckillGoodsList.length; i++) {
                var a = res.data.seckillGoodsList[i].activityPrice
                var specIntegerPrize = a.substring(0, a.indexOf('.'))
                var specFloatPrize = a.substring(a.indexOf('.'))
                stateActivitySeckillHomeData.seckillGoodsList[i].specIntegerPrize = specIntegerPrize
                stateActivitySeckillHomeData.seckillGoodsList[i].specFloatPrize = specFloatPrize
            }
            // state.ActivitySeckillHomeData = stateActivitySeckillHomeData
            commit('change_ActivitySeckillHomeData', stateActivitySeckillHomeData)
            if (state.ActivitySeckillRefreshCount < 3) {
                let time = setInterval(() => {
                    let timeVal = new Date(state.ActivitySeckillHomeData.activityEndDate).getTime() - new Date().getTime();
                    commit('set_activitySeckillAreaTimeData', timeVal)
                    if (state.timeCount.hours <= '00' && state.timeCount.minutes <= '00' && state.timeCount.seconds <= '00') { //当前秒杀结束
                        clearInterval(time);
                        if (state.ActivitySeckillHomeData.activityEndDate !== null) {
                            dispatch('actActivitySeckillHome')
                        }
                    }
                }, 1000)
            }
        }).catch(error => {

            console.log(error)
        })
    },
    activitySeckillAreaTime({
        state,
        commit,
        dispatch
    }, callback) { //秒杀专区-时间段
        ActivitySeckillAreaTime(callback).then(res => {
            if (!(res && res.code == 200)) return;
            state.activitySeckillAreaTimeData.currentDateStr = res.data.currentDateStr
            let time = setInterval(() => {
                let timeVal = new Date(res.data.activityEndDate.replace(/-/g, '/')).getTime() - new Date().getTime();
                state.timeCount.hours = parseInt(timeVal / 1000 / 60 / 60 % 24, 10).toString().padStart(2, '0'); //计算剩余的小时
                state.timeCount.minutes = parseInt(timeVal / 1000 / 60 % 60, 10).toString().padStart(2, '0'); //计算剩余的分钟
                state.timeCount.seconds = parseInt(timeVal / 1000 % 60, 10).toString().padStart(2, '0'); //计算剩余的秒数
                if (state.timeCount.hours <= '00' && state.timeCount.minutes <= '00' && state.timeCount.seconds <= '00') {    //当前秒杀结束
                    clearInterval(time);
                    if (state.activitySeckillAreaTimeData.currentDateStr !== null) {
                        dispatch('activitySeckillAreaTime')
                    }
                }
            }, 1000)
            if (res.data.soonTimeSoltList.length) {
                commit('change_activitySeckillAreaTimeData', res.data)
            }
            let obj = {
                page: 1,
                sessionId: res.data.sessionId,
            }
            dispatch('activitySeckillCurrentGoods', obj) //当前秒杀的商品

            callback();

        }).catch(error => {

            console.log(error)
        })
    },
    activitySeckillCurrentGoods({
        state,
        commit,
        dispatch
    }, objVal) { //秒杀专区-正在秒杀商品列表
        let obj = {
            page: objVal.page ? objVal.page : state.page,
            limit: state.limit,
            sessionId: objVal.sessionId
        }
        if (obj.page == 1) {
            state.ActivitySeckillCurrentGoodsData = [];
        }


        if ((objVal.page ? objVal.page : 1) * 25 >= state.total) state.iscompleted = true; //没有更多数据了

        ActivitySeckillCurrentGoods(obj).then(res => {
            if (!(res && res.code == 200)) return;
            if (res.data.total) state.total = res.data.total;
            state.ActivitySeckillCurrentGoodsData = state.ActivitySeckillCurrentGoodsData.concat(res.data.list);
        }).catch(error => {

            console.log(error)
        })
    },
    activitySeckillSoonGoods({
        state,
        commit,
        dispatch
    }, objVal) { //秒杀专区-即将秒杀商品列表
        let obj = {
            page: objVal.page ? objVal.page : state.page,
            limit: state.limit,
            sessionId: objVal.sessionId
        }
        if (obj.page == 1) {
            // state.ActivitySeckillCurrentGoodsData = [];
            commit('change_ActivitySeckillCurrentGoodsData', [])
        }
        ActivitySeckillSoonGoods(obj).then(res => {
            if (!(res && res.code == 200)) return;
            if (res.data.total) state.total = res.data.total;
            // state.ActivitySeckillCurrentGoodsData = state.ActivitySeckillCurrentGoodsData.concat(res.data.list);
            commit('change_ActivitySeckillCurrentGoodsData', state.ActivitySeckillCurrentGoodsData.concat(res.data.list))
        }).catch(error => {

            console.log(error)
        })
    },
    actDeleteCart({
        state,
        commit,
        dispatch
    }, id) { //首页购物车删除
        DeleteCart(id).then(res => {
            if (!(res && res.code == 200)) return;
            dispatch('actCartList')
        }).catch(error => {

            console.log(error)
        })
    },
    // 已登录批量删除和单个删除购物车
    actLoginCartDelete({
        state,
        commit,
        dispatch
    }, obj) {
        LoginCartDelete(obj).then(res => {
            if (!(res && res.code == 200)) return;
            dispatch('actCartList')
        }).catch(error => {

            console.log(error)
        })
    },
    actStoreInfoStoreId({
        state,
        commit,
        dispatch
    }, storeId) { //店铺首页顶部
        StoreInfoStoreId(storeId).then(res => {
            if (!(res && res.code == 200)) return;
            // state.StoreInfoStoreIdData = res.data;
            commit('change_StoreInfoStoreIdData', res.data)
        }).catch(error => {

            console.log(error)
        })
    },
    actStoreRecommend({
        state,
        commit,
        dispatch
    }, storeId) { //店铺推荐专区
        StoreRecommend(storeId).then(res => {
            if (!(res && res.code == 200)) return;
            // state.StoreRecommendData = res.data;
            commit('change_StoreRecommendData', res.data)
        }).catch(error => {

            console.log(error)
        })
    },
    actGoodsStoreClassAll({
        state,
        commit,
        dispatch
    }, storeId) { //店铺商品分类
        GoodsStoreClassAll(storeId).then(res => {
            if (!(res && res.code == 200)) return;
            // state.GoodsStoreClassAllData = res.data;
            commit('change_GoodsStoreClassAllData', res.data)
        }).catch(error => {

            console.log(error)
        })
    },
    actActivitySeckillRemind({
        state,
        commit,
        dispatch
    }, id) { //秒杀专区-设置提醒
        ActivitySeckillRemind(id).then(res => {
            if (!(res && res.code == 200)) return;
        }).catch(error => {
            console.log(error)
        })
    },
}

// 改变
let mutations = {
    change_IndexNavList(state, value) {
        state.IndexNavList = value
    },
    change_IndexFloorData(state, value) {

        state.actIndexFloorData = value
        state.IndexFloorData = value
    },
    change_ActivitySeckillCurrentGoodsData(state, value) {
        state.ActivitySeckillCurrentGoodsData = value
    },
    change_StoreInfoStoreIdData(state, value) {
        state.StoreInfoStoreIdData = value
    },
    change_StoreRecommendData(state, value) {
        state.StoreRecommendData = value
    },
    change_GoodsStoreClassAllData(state, value) {
        state.GoodsStoreClassAllData = value
    },
    change_IndexCustomClassData(state, value) {
        // console.log('这是mutations中的value11')
        // console.log(state, value)
        state.IndexCustomClassData = value
        state.IndexCustomClassData[i].advLinkConfigDTOList = value[i].advDTO.advLinkConfigDTOList;
    },
    change_ActivitySeckillHomeData(state, value) {
        state.ActivitySeckillHomeData = value
    },
    changeIndexSiteData(state, value) {
        state.IndexSiteData = value
    },
    // 是否领取优惠券
    change_Coupon(state, index) {
        state.ActivityCouponCenterData[index].isReceived = true;
    },
    change_IndexSearchkeyword(state, val) {
        state.IndexSearchkeyword = val;
    },

    //购物车
    change_CartListData(state, val) {
        state.CartListData = val;
    },
    //秒杀点击切换时间段后
    change_activitySeckillAreaTimeData(state, val) {
        state.activitySeckillAreaTimeData = val;

        //第二天叫明日 大于第二天叫 几日
        state.activitySeckillAreaTimeData.soonTimeSoltList.forEach((item, index) => {
            let curTime = new Date(item.activityStartDate.replace(/-/g, '/')).getTime() - new Date().getTime();
            let dayTime = Math.floor(curTime / 1000 / 60 / 60 / 24);
            state.activitySeckillAreaTimeData.soonTimeSoltList[index].dayTime = dayTime > 1 ?
                item.activityStartDate.toString().substring(11, 16) + "日" : dayTime == 0 ?
                    '' : dayTime;
        })
    },
    set_activitySeckillAreaTimeData(state, timeVal) {
        state.ActivitySeckillRefreshCount++
        state.timeCount.hours = parseInt(timeVal / 1000 / 60 / 60 % 24, 10).toString().padStart(2, '0'); //计算剩余的小时
        state.timeCount.minutes = parseInt(timeVal / 1000 / 60 % 60, 10).toString().padStart(2, '0'); //计算剩余的分钟
        state.timeCount.seconds = parseInt(timeVal / 1000 % 60, 10).toString().padStart(2, '0'); //计算剩余的秒数
    }

}
// 获取
let getters = {
    getIndexSiteData(state) {
        return state.IndexSiteData
    },
    getTimeCount(state) {
        return state.timeCount
    }
}
export default {
    namespaced,
    actions,
    state,
    mutations,
    getters
}
