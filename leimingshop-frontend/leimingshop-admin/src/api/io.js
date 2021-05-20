import http from '@/utils/request'

let base = "/admin-api";
let allBase = window.SITE_CONFIG['apiURL'] + "/admin-api";
let requestType = { headers: { 'content-type': 'application/x-www-form-urlencoded' } };

// 导入商品备案信息
export const importRegisterUrl = `${allBase}/goods/register/import`;
//  导出店铺
export const storeExport = `${allBase}/store/export`;

/*导出--商品*/
export const goodsExport = `${allBase}/goods/export`;
/*导出--展示分类模板*/
export const goodsClassCustomExport=`${allBase}/goods/classcustom/export/template`;
/*导入--展示分类*/
export const goodsClassCustomImport=`${allBase}/goods/classcustom/import`;
/*导出--商品审核*/
export const goodsCheckExport = `${allBase}/goods/check/export`;
// 商品分类模板导出
export  const  goodsClassTemplateEx= `${allBase}/goodsclass/export/template`
// 商品分类模板导入
export  const goodsClassImport = `${allBase}/goodsclass/import`
/*导出--品牌*/
export const brandExport = `${allBase}/brand/export`;

/*导出--订单*/
export const orderExport = `${allBase}/order/export`;

/*导出--订单售后退货列表*/
export const afterReturnExport = `${allBase}/aftersale/return/export`;

/*导出--改价记录*/
export const priceLogExport = `${allBase}/price/log/export`;

/*导出--改库存记录*/
export const stockLogExport = `${allBase}/stock/log/export`;

/*导出--售后审核列表*/
export const aftersaleAuditExport = `${allBase}/aftersale/audit/export`;

/*导出--仲裁列表*/
export const arbitrationRecordExport = `${allBase}/arbitration/export`;

/*导出--订单售后换货列表*/
export const aftersaleBarterExport = `${allBase}/aftersale/barter/export`;

/*导出--商品评价*/
export const goodsEvaluateExport = `${allBase}/evaluate/export`;

/*导出--会员列表*/
export const memberExport = `${allBase}/member/export`;

/*导出--登录日志*/
export const memberloginlogExport = `${allBase}/memberloginlog/export`;

/*导出--交易流水*/
export const paymentTallyExport = `${allBase}/payment/tally/export`;

/*导出对账详情-订单列表/退货列表*/
export const billExport = `${allBase}/orderbill/export`;
// 反馈导出
export const feedbackEx = `${allBase}/feedback/export`;
// 投诉导出
export const ordercomplainEx = `${allBase}/ordercomplain/export`;
// 禁用词导入
export const importAdvertisingbanUrl = `${allBase}/stopword/import`;











