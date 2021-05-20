// import http from '@/utils/request'
import{get,post,queryPost}from '@/utils/http'

let base = process.env.BASE_URL
// let front = ''
let portal = `${base}/web/api`
// if(process.env.NODE_ENV !== 'production'){
//     front = `${base}/front`
// }else{
//     front = `${base}`
// }

// 上传图片--------------------------------------------------------------------------------------------------------------------
//Base64上传图片
export const getindexData = (params) => { return post(`${base}/picture/base64123`, params).then(res => res.data); };
//  上传文件
export const uploadFile = (params, progressCb) => { return post(`${base}/picture/video`, params, { onUploadProgress: progressCb }).then(res => res.data); };
//手机号密码登录
export const captchaLogin = (params) => { return queryPost(`${portal}/auth/oauth/captcha`, params).then(res => res.data); };
//手机号登录获取验证码
export const memberMobileCode = (params) => { return get(`${base}/web/api/member/mobile/code`, params).then(res => res.data); };
//手机验证码登录
export const authenticationMobile = (params) => { return queryPost(`${portal}/auth/authentication/mobile`, params).then(res => res.data); };
//退出登录
export const logout = (params) => { return queryPost(`${portal}/auth/logout`, params).then(res => res.data); };
//微信登录请求
export const wechatPcLogin = (params) => { return queryPost(`${portal}/auth/wechat/pc/login`, params).then(res => res.data); };
//微信绑定手机号验证码
export const loginMobileBind = (params) => { return get(`${base}/web/api/member/account/mobile/bind/${params}`).then(res => res.data); };
//绑定邮箱
export const memberBindEmail = (params) => { return get(`${base}/web/api/member/bind/email`,params).then(res => res.data); };
//请求地址四级联动数据
export const getAddressThreelist = (params) => {return get(`${base}/web/api/operation/mini/all`, params).then(res => res);};

