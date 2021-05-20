import { get, post, put, deleteFn, deleteFnOther } from '@/utils/http.js';

let base = process.env.BASE_URL
//wangtao-start
//我的积分详情
export const getMyPointsDetail = (params) => { return get(`${base}/web/api/member/point/log/detail`, params).then(res => res); }
//最近一周我的积分
export const getMyPointsWeek = (params) => { return get(`${base}/web/api/member/point/log/recent`, params).then(res => res); }
//积分明细
export const getPointsDetails = (params) => { return get(`${base}/web/api/member/point/log/page`, params).then(res => res); };
//地址列表
export const getAddressList = (params) => { return get(`${base}/web/api/member/address/page`, params).then(res => res.data); };
//新增地址保存
export const getAddressSaving = (params) => { return post(`${base}/web/api/member/address`, params).then(res => res); };
//会员地址删除(delete)
export const deleteAddressDeletion = (params) => {
    return deleteFnOther(`${base}/web/api/member/address`, params).then(res => res);
};
//会员地址修改(put)
export const putAddressModification = (params) => {
    return put(`${base}/web/api/member/address`, params).then(res => res);
};
//根据地址id设置用户默认地址(put)
export const setDefaultAddress = (params) => {
    return put(`${base}/web/api/member/address/default?id=${params.id}`).then(res => res);
};
//编辑详情根据id查看地址信息(get)
export const getEditInfo = (params) => {
    return get(`${base}/web/api/member/address/${params.id}`).then(res => res.data);
};
//获取地址四级列表全部
export const getAddressThreelist = (params) => {
    return get(`${base}/web/api/operation/mini/all`, params).then(res => res);
};
//获取地址四级列表（根据id）
export const getThreelistId = (params) => {
    return get(`${base}/web/api/operation/area/parent/${params.id}`, params).then(res => res);
};
//用户店铺收藏列表
export const getCollectionShopList = (params) => { return get(`${base}/web/api/member/store/favorites/page`, params).then(res => res); }
//取消店铺收藏（delete）
export const deleteCollecStoreList = (params) => {
    return deleteFn(`${base}/web/api/member/store/favorites`,params).then(res => res);
};
//用户收藏商品列表
export const getCollectionsList = (params) => { return get(`${base}/web/api/member/goods/favorites/page`, params).then(res => res); }
//取消商品收藏(delete)
export const deleteCollectionsList = (params) => {
    return deleteFn(`${base}/web/api/member/goods/favorites`, params).then(res => res);
};
//商品收藏加购物车
export const postCart = (params) => {
    return post(`${base}/web/api/cart`, params).then(res => res.data);
};
//我的足迹列表(可以上拉加载)
export const getMytracksList = (params) => { return get(`${base}/web/api/member/browse/page`, params).then(res => res); }

//删除浏览记录(delete)
export const deleteBrowseList = (params) => {
    return deleteFn(`${base}/web/api/member/browse`, params).then(res => res);
};
//wangtao--end





