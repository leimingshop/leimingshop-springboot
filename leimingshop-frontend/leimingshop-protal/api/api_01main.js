import { get, post, queryPost, deleteFnOther, deleteFn } from '@/utils/http'

let base = process.env.BASE_URL
// let front = ''
// if (process.env.NODE_ENV !== 'production') {
//     front = `${base}/front`
// } else {
//     front = `${base}`
// }

//首页导航条
export const IndexNav = (params) => { return get(`${base}/web/api/operation/index/navigation?${params}`).then(res => res.data); };
//首页轮播图
export const IndexAdv = (params) => { return get(`${base}/web/api/operation/index/adv?advKey=${params.advKey}&${params.storeId ? `storeId = ${params.storeId}` : ''}`, params).then(res => res.data); };
//首页商品分类
export const IndexCustomClass = (params) => { return get(`${base}/web/api/operation/index/custom/class`, params).then(res => res.data); };
//获取首页楼层信息  GET
export const IndexFloor = (params) => { return get(`${base}/web/api/operation/index/floor`, params).then(res => res.data); };
//首页商品推荐列表
export const IndexRecommendPage = (params) => { return get(`${base}/web/api/operation/index/recommend/page?page=${params.page}&limit=${params.limit}`, params).then(res => res.data); };
//热词列表
export const IndexHotWord = (params) => { return get(`${base}/web/api/operation/index/hot/word`, params).then(res => res.data); };
//站点信息
export const IndexSite = (params) => { return get(`${base}/web/api/operation/index/site`, params).then(res => res.data); };
//首页购物车
export const CartList = (params) => { return get(`${base}/web/api/cart/list`, params).then(res => res.data); };
//没有登录删除购物车里的商品
export const DeleteCart = (params) => { return deleteFn(`${base}/web/api/cart`, params).then(res => res.data); };
//登录后删除购物车里的商品
export const LoginCartDelete = (params) => { return deleteFn(`${base}/web/api/cart/delete?delType=${params.delType}&cartId=${params.cartId}`, params).then(res => res.data); };
//搜索智能匹配接口
export const IndexTips = (params) => { return get(`${base}/web/api/search/tips`, params).then(res => res.data); };





//领券中心
export const ActivityCouponCenter = (params) => { return get(`${base}/web/api/activity/coupon/center`, params).then(res => res.data); };
//领取优惠券
export const ActivityCouponReceive = (params) => { return post(`${base}/web/api/activity/coupon/receive/${params.id}`, params).then(res => res.data); };



//首页秒杀中心
export const ActivitySeckillHome = (params) => { return get(`${base}/web/api/activity/seckill/home`, params).then(res => res.data); };
//秒杀专区-时间段
export const ActivitySeckillAreaTime = (params) => { return get(`${base}/web/api/activity/seckill/area/time`, params).then(res => res.data); };
//秒杀专区-正在秒杀商品列表
export const ActivitySeckillCurrentGoods = (params) => { return get(`${base}/web/api/activity/seckill/current/goods`, params).then(res => res.data); };
//秒杀专区-即将秒杀商品列表
export const ActivitySeckillSoonGoods = (params) => { return get(`${base}/web/api/activity/seckill/soon/goods`, params).then(res => res.data); };
//秒杀专区-设置提醒
export const ActivitySeckillRemind = (params) => { return post(`${base}/web/api/activity/seckill/remind/`, params).then(res => res.data); };





//店铺首页顶部
export const StoreInfoStoreId = (params) => { return get(`${base}/web/api/store/info/${params.storeId}`, params).then(res => res.data); };
//店铺推荐专区
export const StoreRecommend = (params) => { return get(`${base}/web/api/store/recommend`, params).then(res => res.data); };
//店铺商品分类
export const GoodsStoreClassAll = (params) => { return get(`${base}/web/api/goods/store/class/all`, params).then(res => res.data); };
//获取未读消息数量
export const messageCount = params => { return get(`${base}/web/api/member/message/count`, params).then(res => res.data); };


//首页专题
export const topicInfoApi = params => { return get(`${base}/web/api/operation/index/topic/info`, params).then(res => res.data); };




