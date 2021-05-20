import{get,post,queryPost,put}from '@/utils/http.js'
let base = process.env.BASE_URL;

// 评价晒单待评价的分页订单商品列表
export const getNoevaluatedList = (params) => {return get(`${base}/web/api/member/evaluate/order/page`, params).then(res => res.data);};
// 评价晒单已评价的分页订单商品列表
export const getEvaluatedList = (params) => {return get(`${base}/web/api/member/evaluate/page`, params).then(res => res.data);};
// 我要评价
export const getEvaluatedId = (params) => {return get(`${base}/web/api/member/evaluate/order/goods/${params.orderId}?orderGoodsId=${params.orderGoodsId}`).then(res => res.data);};	
// 提交评价
export const postNewEvaluated = (params) => {return post(`${base}/web/api/member/evaluate`,params).then(res => res.data);};
// 查看评价
export const getLookEvaluated = (params) => {return get(`${base}/web/api/member/evaluate/${params}`).then(res => res.data);};