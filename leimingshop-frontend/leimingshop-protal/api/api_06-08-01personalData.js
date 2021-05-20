// import http from '@/utils/request'
import{get,post,queryPost,put}from '@/utils/http.js'
let base = process.env.BASE_URL;
//获得个人中心数据
export const homeMember = params => { return get(`${base}/web/api/member/member/home`, params).then(res => res.data); };
//获得公共头部个人数据
export const memberHeader = params => { return get(`${base}/web/api/member/header/info`).then(res => res.data); };
//Base64上传图片
export const getindexData = (params) => { return post(`${base}/picture/base64123`, params).then(res => res.data); };
//获得我的订单信息
export const orderPage = params => { return get(`${base}/web/api/order/page?page=${params.page}&limit=${params.limit}`).then(res => res.data); };
//获取最近收藏商品
export const favoritesPage = params => { return get(`${base}/web/api/member/goods/favorites/page?page=${params.page}&limit=${params.limit}`).then(res => res.data); };
//获取我的足迹
export const browseList = params => { return get(`${base}/web/api/member/browse/list`,params).then(res => res.data); };
//个人信息修改
export const infoBase = params => { return put(`${base}/web/api/member/base/info`, params).then(res => res.data); };
//根据父ID查询地址
export const parentAdress = params => { return get(`${base}/web/api/operation/area/parent`, params).then(res => res.data); };
//查询所有地区信息
export const treeAdress = params => { return get(`${base}/web/api/operation/area/tree`, params).then(res => res.data); };
//上传图片
export const uploadBase64 = params => { return post(`${base}/web/api/order/base64`, params).then(res => res.data); };
