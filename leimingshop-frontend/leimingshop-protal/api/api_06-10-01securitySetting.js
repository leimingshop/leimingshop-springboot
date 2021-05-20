import{get,post,queryPost,queryPut,put,deleteFn}from '@/utils/http.js'
let base = process.env.BASE_URL;

//获取用户发票列表
export const changePassword = params => { return put(`${base}/web/api/member/change/password?oldPwd=${params.oldPwd}&newPwd=${params.newPwd}&ConfirmPwd=${params.ConfirmPwd}`).then(res => res.data); };
//获取短信验证码
export const mobileCode = (params) => { return get(`${base}/web/api/member/mobilecode?mobile=${params.mobile}&codeType=${params.codeType}`).then(res => res.data); };
//修改手机号验证码校验
export const verifyCode = (params) => { return queryPut(`${base}/web/api/member/verify/code?mobile=${params.mobile}&code=${params.code}&codeType=${params.codeType}`).then(res => res.data); };
//获取邮箱地址
export const mailInfo = (params) => { return get(`${base}/web/api/member/mail/info`,params).then(res => res.data); };
//修改邮箱验证码校验
export const changemail = (params) => { return get(`${base}/web/api/member/changemail?email=${params.email}&codeType=${params.codeType}`).then(res => res.data); };
//修改邮箱验证码校验
export const changemailCode = (params) => { return queryPut(`${base}/web/api/member/changemail?email=${params.email}&code=${params.code}&codeType=${params.codeType}`).then(res => res.data); };
//绑定邮箱
export const mobileBind = (params) => { return get(`${base}/web/api/member/send/mailcode/${params.email}`).then(res => res.data); };
