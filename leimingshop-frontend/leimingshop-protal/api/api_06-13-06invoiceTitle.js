import{get,post,queryPost,put,queryPut,deleteFn}from '@/utils/http.js'
let base = process.env.BASE_URL;

//获取用户发票列表
export const memberInvoiceList = params => { return get(`${base}/web/api/member/memberinvoice/list`,params).then(res => res.data); };
//获取订单是否可以开票
export const invoiceCheck = params => { return get(`${base}/web/api/order/invoice/check/${params}`).then(res => res.data); };
//保存或修改发票
export const getInvoice = params => { return post(`${base}/web/api/member/memberinvoice`,params).then(res => res.data); };
//删除用户发票
export const deleteInvoice = params => { return deleteFn(`${base}/web/api/member/memberinvoice`,params).then(res => res.data); };
//查看发票详情
export const memberInvoiceDetail = params => { return get(`${base}/web/api/member/memberinvoice/detail/${params.id}`).then(res => res.data); };
//获取默认发票
export const memberInvoiceDefault = params => { return get(`${base}/web/api/member/memberinvoice/default`,params).then(res => res.data); };
//设为默认发票
export const settingDefault = params => {return put(`${base}/web/api/member/memberinvoice/setting/default?invoiceId=${params.invoiceId}`,params).then(res => res.data)}

//我的发票
//订单可申请售后服务列表
export const invoiceAfter = params => { return get(`${base}/web/api/order/invoice/page?page=${params.page}&limit=${params.limit}`).then(res => res.data); };
//申请发票接口
export const invoiceApply = params => { return post(`${base}/web/api/order/invoice/apply`,params).then(res => res.data); };
//申请换开发票接口
export const changeInvoiceApply = params => { return put(`${base}/web/api/order/invoice`,params).then(res => res.data); };
//查看发票详情接口
export const orderInvoice = params => { return get(`${base}/web/api/order/invoice/${params}`).then(res => res.data); };
//查看当前发票换开地址
export const invoiceInfo = params => { return get(`${base}/web/api/store/invoice/info/${params}`).then(res => res.data); };
//发送邮件
export const sendMail = params => { return queryPut(`${base}/web/api/order/invoice/send/mail`,params).then(res => res.data); };