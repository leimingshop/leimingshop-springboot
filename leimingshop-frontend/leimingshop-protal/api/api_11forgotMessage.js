import{get,post,queryPost,queryPut}from '@/utils/http'

let base = process.env.BASE_URL
let portal = `${base}/web/api`
// let front = ''
// if(process.env.NODE_ENV !== 'production'){
//     front = `${base}/front`
// }else{
//     front = `${base}`
// }

//https://b2b2c.leimingtech.com/front/member/register/verify?memberMobile=18132133654&loginType=2
//校验手机号是否已注册
export const verifyRegister = (params) => { return queryPost(`${base}/web/api/member/register/verify`, params).then(res => res.data); };
//校验手机号验证码
export const verifyCaptcha = (params) => { return get(`${base}/web/api/member/register/verify/captcha`, params).then(res => res.data); };
//获取短信验证码
export const mobileCode = (params) => { return get(`${base}/web/api/member/register/update/password/mobile/code`, params).then(res => res.data); };
//校验手机号和短信验证码
export const registerCompareCode = (params) => { return queryPost(`${base}/web/api/member/register/compare/code`, params).then(res => res.data); };
//修改密码
export const forgetPassword = (params) => { return queryPut(`${base}/web/api/member/register/forget/password`, params).then(res => res.data); };
