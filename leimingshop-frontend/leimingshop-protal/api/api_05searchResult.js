import { get, post } from '@/utils/http.js';

let base = process.env.BASE_URL
let portal = `${base}/web/api`
// let front = ''
// if(process.env.NODE_ENV !== 'production'){
//     front = `${base}/front`
// }else{
//     front = `${base}`
// }


// 商品搜索
export const searchGoods = (params) => { return post(`${portal}/search/goods`, params).then(res => res.data); };

// 商品过滤
export const filterGoods = (params) => { return post(`${portal}/search/filter`, params).then(res => res.data); };

// 店铺搜索
export const searchStore = (params) => { return post(`${portal}/search/store`, params).then(res => res.data); };

// 热门推荐
export const hotRecommendList = (params) => { return get(`${portal}/operation/hot/list`).then(res => res.data); };

// 满减活动去凑单商品列表页面
export const collectBillsReduce = (params) => { return post(`${portal}/search/collect/bills/reduce`, params ).then(res => res.data); }

