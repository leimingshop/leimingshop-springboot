import{get,post,queryPost}from '@/utils/http'

let base = process.env.BASE_URL

export const registerMobileCode = (params) => { return get(`${base}/web/api/member/register/mobile/code`, params).then(res => res.data); };//注册获取手机验证码
export const memberRegister = (params) => { return queryPost(`${base}/web/api/member/register`, params).then(res => res.data); };//用户注册
export const noticeProtocol = (params) => { return get(`${base}/web/api/member/register/notice/protocol`, params).then(res => res.data); };//用户注册获取用户协议
