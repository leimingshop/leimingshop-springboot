
import{get,post,queryPost,put,deleteFn}from '@/utils/http.js'

let base = process.env.BASE_URL

// 上传图片--------------------------------------------------------------------------------------------------------------------
//Base64上传图片
export const getindexData = (params) => { return post(`${base}/picture/base64123`, params).then(res => res.data); };
//  上传文件
export const uploadFile = (params, progressCb) => { return post(`${base}/picture/video`, params, { onUploadProgress: progressCb }).then(res => res.data); };

//上传图片
export const uploadBase64 = params => { return post(`${base}/web/api/order/base64`, params).then(res => res.data); };

// 提交保存反馈
export const saveFeedback = params => {return post(`${base}/web/api/feedback`, params).then(res => res.data); };
