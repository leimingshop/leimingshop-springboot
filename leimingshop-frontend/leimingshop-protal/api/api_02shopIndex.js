import { get, post } from '@/utils/http.js'

let base = process.env.BASE_URL;

// 店铺分类
export const storeClass = (params) => { return get(`${base}/web/api/goods/store/class/all?storeId=${params}`).then(res => res.data); };

// 店铺商品推荐
export const goodsRecommend = (params) => { return get(`${base}/web/api/goods/recommend?storeId=${params}`).then(res => res.data); };
