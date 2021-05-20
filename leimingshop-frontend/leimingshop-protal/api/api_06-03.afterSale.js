import { get, post } from '@/utils/http.js'
import qs from 'qs'

let base = process.env.BASE_URL
// let front = ''
let portal = `${base}/web/api`
// if(process.env.NODE_ENV !== 'production'){
//     front = `${base}/front`
// }else{
//     front = `${base}`
// }

let requestType = { headers: { 'content-type': 'application/x-www-form-urlencoded' } };

// 取消售后申请
export const aftersaleApplyCancel = (params) => { return post(`${portal}/order/aftersale/apply/cancel/${params}`).then(res => res.data); };

// 售后详情
export const aftersaleApplyDetail = (params) => { return get(`${portal}/order/aftersale/apply/detail?${qs.stringify(params)}`).then(res => res.data); };

// 售后进度
export const aftersaleApplyProcess = (params) => { return get(`${portal}/order/aftersale/apply/process/${params}`).then(res => res.data); };

// 售后服务申请记录
export const aftersaleApplyRecord = (params) => { return get(`${portal}/order/aftersale/apply/record?${qs.stringify(params)}`).then(res => res.data); };

// 保存售后保存
export const aftersaleApplySave = (params) => { return post(`${portal}/order/aftersale/apply/save`, params).then(res => res.data); };

// 订单可申请售后服务列表
export const aftersaleAvailable = (params) => { return get(`${portal}/order/aftersale/available/aftersale?${qs.stringify(params)}` ).then(res => res.data); };

// H5端售后买家上传物流信息
export const sellerDeliverySave = (params) => { return post(`${portal}/order/aftersale/seller/delivery/save`, params).then(res => res.data); };

// 订单中可申请售后商品详情
export const afterSaleOrderInfo = (params) => { return get(`${portal}/order/available/aftersale/${params}`).then(res => res.data); };

// 保存售后申请
export const afterApplySave = (params) => { return post(`${portal}/order/aftersale/apply/save`, params).then(res => res.data); };

// H5端售后原因接口
export const orderApplyReason = (params) => { return get(`${portal}/order/apply/reason?${qs.stringify(params)}` ).then(res => res.data); };

// pc会员地址列表
export const addressList = (params) => { return get(`${portal}/member/address/page?${qs.stringify(params)}` ).then(res => res.data); };

//确认收货
export const orderConfirmReceipt = (params) => { return post(`${portal}/order/aftersale/apply/confirm/receipt/${params}`).then(res => res.data); };
