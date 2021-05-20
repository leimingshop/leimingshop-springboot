import{get,post,queryPost,put,deleteFn}from '@/utils/http.js'
let base = process.env.BASE_URL
// let front = ''
let portal = `${base}/web/api`
// if(process.env.NODE_ENV !== 'production'){
//     front = `${base}/front`
// }else{
//     front = `${base}`
// }




// 个人中心商品-优惠券列表
export const getCouponList = params => {return get(`${base}/web/api/activity/coupon/center/list`, params).then(res => res.data);};
// 商品详情页-优惠券列表
export const getshopCoupons = params => {return get(`${base}/web/api/activity/coupon/goods/detail`, params).then(res => res.data);};
// 个人中心去使用商品优惠券获取商品列表
export const collectBills = params => {return post(`${portal}/search/collect/bills`, params).then(res => res.data);};
