// import http from '@/utils/request'
// import http from '@/utils/request'
import { get, post, queryPost, deleteFn, put, deleteFnOther } from '@/utils/http'

let base = process.env.BASE_URL;
let requestType = { headers: { 'content-type': 'application/x-www-form-urlencoded' } };


//购物车列表
export const CartPage = (params) => { return get(`${base}/web/api/cart/page`, params).then(res => res.data); };
//购物车取消全选或者全选
export const CheckAall = (params) => { return post(`${base}/web/api/cart/check/all?storeId=${params.storeId}&state=${params.state}`, params).then(res => res.data); };
//修改购物车的选中状态
export const UpdateCheck = (params) => { return post(`${base}/web/api/cart/update/check?cartId=${params.cartId}&state=${params.state}`, params).then(res => res.data); };
//购物车商品推荐列表
export const CartRecommend = (params) => { return get(`${base}/web/api/cart/recommend/page`, params).then(res => res.data); };
//加入购物车
export const AddCar = (params) => { return post(`${base}/web/api/cart`, params).then(res => res.data); };
//未登录删除缓存中的购物车
export const CartDeleteNologin = (params) => { return deleteFn(`${base}/web/api/cart`, params).then(res => res.data); };
//已登录批量删除和单个删除购物车
export const CartDelete = (params) => { return deleteFn(`${base}/web/api/cart/delete?delType=${params.delType}&cartId=${params.cartId}`, params).then(res => res.data); };
export const CartDelete1 = (params) => { return deleteFn(`${base}/web/api/cart/delete?delType=${params.delType}`, params).then(res => res.data); };

//购物车移入收藏
export const CartFavorites = (params) => { return post(`${base}/web/api/cart/favorites?cartId=${params.cartId}`, params).then(res => res.data); };
//购物车同步数据
export const CartMergeCart = (params) => { return post(`${base}/web/api/cart/merge/cart`, params).then(res => res.data); };
//领取优惠券
export const Getcoupon = (params) => { return post(`${base}/web/api/activity/coupon/receive/${params.couponId}`).then(res => res.data); };
//优惠券列表
export const GoodscouponList = (params) => { return get(`${base}/web/api/activity/coupon/goods/detail`, params).then(res => res.data); };
// 活动列表
export const GoodsActivityList = (params) => { return get(`${base}/web/api/cart/goods`, params).then(res => res.data); };



//购物车去结算
export const Settlement = (params) => { return get(`${base}/web/api/cart/settlement`, params).then(res => res.data); };
// 立即购买去结算
export const BuyNowSettlement = (params) => { return get(`${base}/web/api/cart/buynow?specId=${params.specId}&number=${params.number}`).then(res => res.data); };

// 根据父id查询地区（ps：一级地区为0） 
export const AreaList = (params) => { return get(`${base}/web/api/operation/area/parent/${params.id}`, params).then(res => res.data); };


//地址列表
export const AddressPage = (params) => { return get(`${base}/web/api/member/address/page`, params).then(res => res.data); };
//修改地址
export const AddressUpdata = (params) => { return put(`${base}/web/api/member/address`, params).then(res => res.data); };
// 地址详情
export const AddressDetail = (params) => { return get(`${base}/web/api/member/address/${params.id}`, params).then(res => res.data); };
//根据地址id设置用户默认地址(put)
export const setDefaultAddress = (params) => {
    return put(`${base}/web/api/member/address/default?id=${params.id}`).then(res => res);
};

//新增地址
export const AddAddress = (params) => { return post(`${base}/web/api/member/address`, params).then(res => res.data); };
//购物车切换优惠活动/地址
export const FillOrderFlush = (params) => { return put(`${base}/web/api/cart/settlement/price/flush`, params).then(res => res.data); };


//获取默认发票
export const DefaultInvoice = (params) => { return get(`${base}/web/api/member/memberinvoice/default`, params).then(res => res.data); };
// 修改发票
export const UpdataInvoice = (params) => { return post(`${base}/web/api/member/memberinvoice`, params).then(res => res.data); };
//发票抬头列表
export const InvoiceList = (params) => { return get(`${base}/web/api/member/memberinvoice/list`, params).then(res => res.data); };
//保存购物车订单
export const SaveOrder = (params) => { return post(`${base}/web/api/order/cart`, params).then(res => res.data); };
// 立即购买切换优惠活动/地址
export const BuynowFlush = (params) => { return put(`${base}/web/api/cart/buynow/price/flush`, params).then(res => res.data); };
//立即购买提交订单
export const OrderBuynow = (params) => { return post(`${base}/web/api/order/buynow`, params).then(res => res.data); };
//待付款订单查询支付标识
export const checkPaySign = (params) => { return get(`${base}/web/api/order/check/${params}`).then(res => res.data); };
//待付款订单去支付
export const toPay = (params) => { return post(`${base}/web/api/payment/pay/topay/${params}`).then(res => res.data); };
//获取支付方式
export const payType = (params) => { return get(`${base}/web/api/payment/pay/type/WEB`, params).then(res => res.data); };
//微信支付
export const wechatPay = (params) => { return post(`${base}/web/api/payment/pc`, params).then(res => res.data); };
//微信支付
export const aliPay = (params) => { return post(`${base}/web/api/payment/pcpay`, params).then(res => res.data); };



























