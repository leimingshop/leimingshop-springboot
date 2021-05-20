import { get, post,  deleteFn} from '@/utils/http.js'
import qs from 'qs'

let base = process.env.BASE_URL
// let front = ''
let portal = `${base}/web/api`
// if(process.env.NODE_ENV !== 'production'){
//     front = `${base}/front`
// }else{
//     front = `${base}`
// }

// 商品详情
export const goodsDetails = (params) => { return get(`${portal}/search/goods/details?${qs.stringify(params)}`).then(res => res.data); };

// 优惠券列表
export const goodsDetailsCoupon = (params) => { return get(`${portal}/activity/coupon/goods/detail?${qs.stringify(params)}`).then(res => res.data); };

// 商品可选满减活动列表
export const goodsReduceActivityList = (params) => { return get(`${portal}/cart/goods?${qs.stringify(params)}`).then(res => res.data); };

// 店铺热卖
export const shopGoodsHot = (params) => { return get(`${portal}/goods/hot?${qs.stringify(params)}`).then(res => res.data); }

// 同类推荐
export const shopGoodsLike = (params) => { return get(`${portal}/goods/like?${qs.stringify(params)}`).then(res => res.data); }

// 收藏商品
export const goodsCollection = (params) => { return post(`${portal}/member/goods/favorites`, params).then(res => res.data); }

// 取消收藏商品
export const goodsCancelCollection = (params) => { return deleteFn(`${portal}/member/goods/favorites`, params).then(res => res.data); }

// 查询商品收藏数量
export const goodsFavNum = (params) => { return get(`${portal}/goods/fav/num?${qs.stringify(params)}`).then(res => res.data); }

// 查询商品是否被收藏
export const goodsIsCollected = (params) => { return get(`${portal}/member/goods/favorites/isfavorites?${qs.stringify(params)}`).then(res => res.data); }

// 关注店铺
export const storeCollection = (params) => { return post(`${portal}/member/store/favorites`, params).then(res => res.data); }

// 取消关注店铺
export const storeCancelCollection = (params) => { return deleteFn(`${portal}/member/store/favorites`, params).then(res => res.data); }

// 加入购物车
export const addToShoppingCart = (params) => { return post(`${portal}/cart`, params).then(res => res.data); }

// 立即购买
export const cartBuyNow = (params) => { return get( `${portal}/cart/buynow?${qs.stringify(params)}` ).then(res => res.data); }

// 店铺详情
export const storeInfo = (params) => { return get(`${portal}/store/info/${params}` ).then(res => res.data); }

// 评价列表
export const goodsCommentPage = (params) => { return get(`${portal}/goods/page?${qs.stringify(params)}` ).then(res => res.data); }

// 新增浏览记录
export const browseRecord = (params) => { return post(`${portal}/member/browse?${qs.stringify(params)}` ).then(res => res.data); }

// 领取优惠券
export const couponReceive = (params) => { return post(`${portal}/activity/coupon/receive/${params}`).then(res => res.data); };

// 根据父级分类ID查询子级数据
export const classifyList = (params) => { return get(`${portal}/goodsclass/children/${params}`).then(res => res.data); };
