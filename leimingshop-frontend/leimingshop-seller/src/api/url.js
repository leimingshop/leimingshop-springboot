// import http from '@/utils/http'
/**
 * 这里是查询的url，用了mixin里面封装的查询
 *
 */

let base ="/seller-api";
// 商品管理--------------------------------------------------------------------------------------------------------------------
 // 商品列表
 export const goodsPageUrl = `${base}/goods/page`;
  // 删除菜单
  export const delMenu = `${base}/menu`;
 // 删除商品
 export const deleteGoodsUrl = `${base}/goods`;
 // 商品审核---------------------------------------------------
 // 商品审核列表
 export const goodsVerifyPageUrl = `${base}/goods/verify/page`;

 // 商品规格管理----------------------------------------------------------------------------------------------------------------
 //分页查询商品SKU列表
 export const goodsSpecPage = `${base}/goods/spec/page`;

 // 价格管理 价格修改记录管理---------------------------------------------------------------------------------------------------
 // 分页查询
 export const priceLogPageUrl = `${base}/price/log/page`;
 // 删除
 export const deletepriceLogUrl = `${base}/price/log`;

 // 库存管理 库存修改记录管理---------------------------------------------------------------------------------------------------
 // 分页查询
 export const stockLogPageUrl = `${base}/stock/log/page`;
 // 删除
 export const deletestockLogUrl = `${base}/stock/log`;

 // 售后说明  售后模板 ---------------------------------------------------------------------------------------------------
 // 分页查询
 export const aftertemplatePageUrl = `${base}/aftertemplate/page`;
 // 删除
 export const deleteAftertemplateUrl = `${base}/aftertemplate`;


 // 属性管理-------------------------------------------------------------------------------------------------------
 //查询属性列表
 export const attributePageUrl = `${base}/attribute/page`;
 // 删除属性
 export const deleteAttributeUrl = `${base}/attribute/batch`;

 //查询列表分组
 export const attributeGroup = `${base}/attributegroup/page`;
 // 删除属性分组
 export const deleteAttributeGroupUrl = `${base}/attributegroup/batch`;



 // 规格管理--------------------------------------------------------------------------------------------------------------------
 //规格列表
 export const specePageUrl = `${base}/spec/page`;
 // 删除规格
 export const deleteSpecUrl = `${base}/spec/batch`;

 //查询规格分组
 export const specGroup = `${base}/specgroup/page`;
 // 删除规格分组
 export const deleteSpecGroupUrl = `${base}/specgroup/batch`;


 // 品牌管理--------------------------------------------------------------------------------------------------------------------
 // 品牌列表
 export const brandPageUrl = `${base}/brand/page`;
 // 编辑品牌
 export const deleteBrandUrl = `${base}/brand`;

 // 商户管理--------------------------------------------------------------------------------------------------------------------
 // 商家列表
 export const businessPageUrl = `${base}/store/page`;
 //商户等级列表
 export const storeGradePageUrl = `${base}/store/grade/page`;

 //  类目管理--------------------------------------------------------------------------------------------------------------------
 //后台分类
 export const goodsclassPageUrl = `${base}/goodsclass/page`;
 // 查询全部一级分类/根据父级分类查出子级分类
 export const allGoodsclassUrl = `${base}/goodsclass/all/goodclass`;
 // 删除分类
 export const deleteGoodsclassUrl = `${base}/goodsclass`;

 //展示分类-------------------------------------------------------------------------------------------------------------------
 //类目列表
 export const categoryUrl = `${base}/goods/classcustom/all`;
 // 删除分类
 export const deleteGoodsclasscustomUrl = `${base}/goods/classcustom`;
  // 删除店铺商品分类分类
  export const deletestoreGoodsClass = `${base}/storegoodsclass`;

 //商品评价-------------------------------------------------------------------------------------------------------------------
//商品评价
export const goodseva= `${base}/evaluate/page`;
//商品评价--删除
export const deleva= `${base}/evaluate`;

//订单管理------------------------------------------------------------------
//订单列表
export const orderlists = `${base}/order/page`;
//售后列表
export const aftersales = `${base}/aftersale/apply/page`;
//退货列表
export const returngoods = `${base}/aftersale/return/page`;
//换货列表
export const exchangegoods = `${base}/aftersale/barter/page`;
//退货管理--导出
export const exportsales= `${base}/aftersale/return/export`;
//申请审核
export const aftersaleAuditPage= `${base}/aftersale/audit/page`
//仲裁管理
export const arbitrationPage = `${base}/arbitration/page`;
//图片库
//分页获取所有图片
export const getallimages= `${base}/picture/page`;
// 上传图片--------------------------------------------------------------------------------------------------------------------
//Base64上传图片   需要传id的
export const uploadPicBase64 = `${base}/picture`;

/*优惠券后期可能删掉con,统一写这里，方便修改*/
//优惠券活动分页查询
export const getCouponPage= `${base}/con/coupons/activity/page`
//满减活动分页查询
export const getFullReductionPage= `${base}/reduce/activity/page`
//优惠券活动分页查询
export const getSeckillActivityPage= `${base}/seckill/activity/page`

//限时抢购活动分页查询
export const getFlashSalePage= `${base}/flash/sale/page`


//拼团活动分页查询
export const getGroupPage= `${base}/group/page`
//拼团活动添加商品   商品列表数据
export const getGroupGoodsPage= `${base}/group/list/goods/add`
//拼团活动 已参加活动列表数据
export const getGroupGoodsList= `${base}/group/selected/goods/list`
//拼团活动记录列表
export const getGroupRecordList= `${base}/group/record/page`
//拼团订单列表
export const ordergrouppage= `${base}/order/group/page`

//电子提货码分页查询
export const getFetchCodePage= `${base}/order/fetchcode/page`
// 核销记录分页查询
export const getVerifyRecordPage= `${base}/order/verify/page`


//发票管理列表
export const getorderinvoicepage = `${base}/orderinvoice/page`
//发票上传接口
export const fileInvoiceUpload = `${base}/file/invoice/upload`

// 导出管理
export const getsysexportmanagement = `${base}/sysexportmanagement/page`;

//根据id删除导出管理信息
export const delSysExportManagement = `${base}/sysexportmanagement`;
