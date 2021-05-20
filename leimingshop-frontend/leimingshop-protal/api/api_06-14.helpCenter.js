import{get,post,queryPost,put,deleteFn}from '@/utils/http.js'
let base = process.env.BASE_URL

// 帮助中心-获取一级分类集合
export const getMyFirstClassList = params => { return get(`${base}/web/api/operation/index/firstClassList`,params).then(res => res.data); };

// 帮助中心-分类树分类集合
export const getClassTreeList = params => { return get(`${base}/web/api/operation/index/classTreeList`,params).then(res => res.data); };


// 帮助中心-左侧子分类集合
// export const getMyChildClassList = params => { return get(`${base}/web/api/operation/index/childClassList`,params).then(res => res.data); };

// 帮助中心-分类下的问题列表
export const getMyHelpList = params => { return get(`${base}/web/api/operation/index/help/page`, params).then(res => res.data); };

// 帮助中心-问题详情
export const getquestionDetails = params => { return get(`${base}/web/api/operation/index/help/${params}`).then(res => res.data); };



