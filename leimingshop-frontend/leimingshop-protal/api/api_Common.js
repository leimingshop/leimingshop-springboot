import{get,post,queryPost,put}from '@/utils/http.js'

let base = process.env.BASE_URL

// 上传图片--------------------------------------------------------------------------------------------------------------------
//Base64上传图片
export const uploadPicBase64 = (params, progressCb) => { return post(`${base}/web/api/order/base64`, params, {onUploadProgress:progressCb}).then(res => res.data); };
//  上传文件
export const uploadFile = (params, progressCb) => { return post(`${base}/picture/video`, params, { onUploadProgress: progressCb }).then(res => res.data); };


