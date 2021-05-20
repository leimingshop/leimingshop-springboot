import { get, post, put, deleteFn, deleteFnOther } from '@/utils/http.js';
let base = process.env.BASE_URL;
//我的订单列表
export const OrderPage = (params) => { return get(`${base}/web/api/order/page`, params).then(res => res); }

//删除订单
export const OrderDelete = (params) => {  return deleteFnOther(`${base}/web/api/order/${params}`, params).then(res => res.data); };

//取消订单
export const OrderCancel = (params) => { return put(`${base}/web/api/order/cancel`, params).then(res => res.data); };

//取消原因接口列表
export const OrderCancelReason = (params) => { return get(`${base}/web/api/order/apply/reason`, params).then(res => res.data); };

//确认收货
export const OrderConfirmReceipt = (params) => { return put(`${base}/web/api/order/confirm/receipt/${params}`).then(res => res.data); };

//再次购买
export const CartAgain = (params) => { return post(`${base}/web/api/cart/again`, params).then(res => res.data); };

//订单详情
export const orderDetails= (params) => { return get(`${base}/web/api/order/${params.id}`).then(res => res.data); };

//物流信息
export const orderLogistics= (params) => { return get(`${base}/web/api/order/logistics/${params.id}`).then(res => res.data); };










