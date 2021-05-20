import{get,post,queryPost,put}from '@/utils/http.js'
let base = process.env.BASE_URL;

//获取系统消息
export const messagePage = params => { return get(`${base}/web/api/member/message/page?page=${params.page}&limit=${params.limit}`).then(res => res.data); };
//获取系统消息
export const messageDetail = params => { return get(`${base}/web/api/member/message/detail?messageId=${params.messageId}`).then(res => res.data); };
//获取未读消息数量
export const messageCount = params => { return get(`${base}/web/api/member/message/count`,params).then(res => res.data); };
