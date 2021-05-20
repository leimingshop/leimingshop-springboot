import http from '@/utils/request'

let base = "/seller-api";
let allBase = window.SITE_CONFIG['apiURL'] + "/seller-api";
let requestType = { headers: { 'content-type': 'application/x-www-form-urlencoded' } };

/*导出--商品*/
export const goodsExport = `${allBase}/goods/export`;
/*导入--商品*/
export const goodsImport=`${allBase}/goods/import`;
/*导出 -- 商品模板*/
export const goodsExcelTemplateExport=`${allBase}/goods/download/template`;

/*导出--商品审核*/
export const goodsCheckExport = `${allBase}/goods/check/export`;
/*导出--店铺商品分类*/
export const storeGoodsClassExport=`${allBase}/storegoodsclass/export/template`
/*导入-- 店铺商品分类*/
export const storeGoodsClassImport=`${allBase}/storegoodsclass/import`

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

/*导出--售后审核列表*/
export const arbitrationRecordExport = `${allBase}/arbitration/export`;

/*导出--订单售后换货列表*/
export const aftersaleBarterExport = `${allBase}/aftersale/barter/export`;

/*导出--商品评价*/
export const goodsEvaluateExport = `${allBase}/evaluate/export`;

/*导出--会员列表*/
export const memberExport = `${allBase}/member/export`;

/*导出--登录日志*/
export const storeloginlogExport = `${allBase}/storeloginlog/export`;

/*导出对账详情-订单列表/退货列表*/
export const billExport = `${allBase}/orderbill/export`;












