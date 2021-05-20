// import http from '@/utils/http'
/**
 * 这里是查询的url，用了mixin里面封装的查询
 *
 */

let base = "/admin-api";

// 商品管理--------------------------------------------------------------------------------------------------------------------
// 商品列表
export const goodsPageUrl = `${base}/goods/page`;

//购物车推荐列表
export const goodsCarlist = `${base}/cartrecom/page`;
//购物车推荐列表批量删除
export const goodsCarlistDelete = `${base}/cartrecom`;

// 底部iCon配置
export const iconCinfigList = `${base}/icon/list`;

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

//消息-------------------------------------------------------------------------------------------------------------
//消息列表分页
export const getmessagepage = `${base}/template/message/page`;
//用户信息
export const getUser = `${base}/template/messageuser`;


// 规格管理--------------------------------------------------------------------------------------------------------------------
//规格列表
export const specePageUrl = `${base}/spec/page`;
// 删除规格
export const deleteSpecUrl = `${base}/spec/batch`;

//查询规格分组
export const specGroup = `${base}/specgroup/page`;
// 删除规格分组
export const deleteSpecGroupUrl = `${base}/specgroup/batch`;


// 标签管理--------------------------------------------------------------------------------------------------------------------
//标签列表
export const tagPageUrl = `${base}/label/page`;
//删除标签
export const deleteTagUrl = `${base}/label`;

//查询标签分组
export const tagGroup = `${base}/labelgroup/page`;
//删除标签分组
export const deletetagGroupUrl = `${base}/labelgroup`;

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


//会员管理
//会员列表
export const memberPageUrl = `${base}/member/page`;
// //会员列表删除
export const deleteMemberUrl = `${base}/member`;
//会员地址
export const memberAddress = `${base}/address/page`;
//会员地址删除
export const deleteAddress = `${base}/address`;
//会员等级
export const memberGrade = `${base}/membergrade/page`;
//登录日志
export const loginLog = `${base}/memberloginlog/page`;
//会员等级删除
export const gradeDel = `${base}/membergrade`;
//会员积分列表
export const memberPointPage = `${base}/point/page`;
//会员余额变更记录
export const memberBalancelogPage = `${base}/memberbalancelog/page`;

//财务管理
//支付方式列表
export const paymentWayUrl = `${base}/payment/list/all`
//交易管理
export const dealListUrl = `${base}/payment/tally/page`
//交易管理--导出
export const dealListExportUrl = `${base}/payment/tally/export`

//内容管理-----------------------------------------------------------------------------------------------------------------------
//站内信查看
export const informationsee = `${base}/message/text/page`;
//站内信删除
export const messagedelete = `${base}/message/text`;
//站内信新增
export const addmessage = `${base}/message/text`;
//站内信新增的会员   除店铺
export const addmessagevip = `${base}/member/page/message`;
//站内信新增的会员   店铺
export const addmessagevipshop = `${base}/store/page`;
//站内信短消息接收人列表
export const informationlist = `${base}/message/receiver/page`;
//站内信短消息接收人删除
export const informationdelete = `${base}/message/receiver`;


//原因管理删除
export const deleteReason = `${base}/reasondescription`;


//订单管理--售后管理
//订单列表
export const orderlists = `${base}/order/page`;
//换货管理
export const exchangegoods = `${base}/aftersale/barter/page`;
//退货管理
export const returngoods = `${base}/aftersale/return/page`;
//申请审核
export const aftersaleAuditPage = `${base}/aftersale/audit/page`;
//售后审核
export const aftersales = `${base}/aftersale/apply/page`;
//仲裁管理
export const arbitrationPage = `${base}/arbitration/page`;
//商品评价
export const goodseva = `${base}/evaluate/page`;
//商品评价--删除
export const deleva = `${base}/evaluate`;
//退货管理--导出
export const exportsales = `${base}/aftersale/return/export`;

//电子提货码分页查询
export const getFetchCodePage= `${base}/order/fetchcode/page`
// 核销记录分页查询
export const getVerifyRecordPage= `${base}/order/verify/page`

//图片库
//分页获取所有图片
export const getallimages = `${base}/picture/page`;
// 上传图片--------------------------------------------------------------------------------------------------------------------
//Base64批量上传图片   需要传id的
export const uploadPicBase64 = `${base}/picture`;

// 系统设置-搜索管理-近义词管理
// 列表
export const shopsynonymUrl = `${base}/synonym/page`;
// 删除
export const deleteShopsynonymUrl = `${base}/synonym`;

// 系统设置-搜索管理-热词管理
// 列表
export const keywordpageUrl = `${base}/hotword/page`;
// 删除
export const deleteShophotkeywordUrl = `${base}/hotword`;



// 店铺审核----------------------
// 列表GET
export const auditPgaeUrl = `${base}/audit/page`;

// 店铺普通信息审核列表
export const auditBasicPgaeUrl= `${base}/audit/basic/page`;

// 店铺公司信息审核列表
export const auditCompanyPgaeUrl = `${base}/audit/company/page`;

// 导出管理
export const getsysexportmanagement = `${base}/sysexportmanagement/page`;

//根据id删除导出管理内容
export const delSysExportManagement = `${base}/sysexportmanagement`;


  // 删除菜单
  export const delMenu = `${base}/store/menu`;

// cms管理-----------------------------------------------------------------------------------------------------------------------
// 运营-服务指南管理-服务指南文章
// 列表
export const serviceGuidePageUrl = `${base}/cms/serviceguide/page`;
// 删除
export const deleteServiceGuideUrl = `${base}/cms/serviceguide/delete`;
// 信息
export const serviceGuideInfoUrl = `${base}/cms/serviceguide`;
// 新增
export const saveServiceGuideUrl = `${base}/cms/serviceguide/save`;
// 修改
export const updateServiceGuideUrl = `${base}/cms/serviceguide/update`;
// 运营-服务指南管理-服务指南分类
// 删除
export const deleteServiceGuideClassUrl = `${base}/cms/serviceguideclass/delete`;
// 信息
export const serviceGuideClassInfoUrl = `${base}/cms/serviceguideclass`;
// 新增
export const saveServiceGuideClassUrl = `${base}/cms/serviceguideclass/save`;
// 修改
export const updateServiceGuideClassUrl = `${base}/cms/serviceguideclass/update`;
// 分类树集合
export const serviceGuideClassTreeListUrl = `${base}/cms/serviceguideclass/classTreeList`;
// 一级分类集合
export const serviceGuideFirstClassListUrl = `${base}/cms/serviceguideclass/firstClassList`;

//运营-资讯管理-资讯文章
// 列表
export const informationPageUrl = `${base}/cms/information/page`;
//删除
export const deleteInformationUrl = `${base}/cms/information/delete`;
// 信息
export const informationInfoUrl = `${base}/cms/information`;
// 新增
export const saveInformationUrl = `${base}/cms/information/save`;
// 修改
export const updateInformationUrl = `${base}/cms/information/update`;
// 文章状态修改
export const statusUpdateInformationUrl = `${base}/cms/information/statusUpdate`;
//运营-资讯管理-资讯分类
// 删除
export const deleteInformationClassUrl = `${base}/cms/informationclass/delete`;
// 信息
export const informationClassInfoUrl = `${base}/cms/informationclass`;
// 新增
export const saveInformationClassUrl = `${base}/cms/informationclass/save`;
// 修改
export const updateInformationClassUrl = `${base}/cms/informationclass/update`;
// 分类树集合
export const informationClassTreeUrl = `${base}/cms/informationclass/zxClassList`;
// 分类树集合（二级）
export const informationClassTreeSecondLevelUrl = `${base}/cms/informationclass/zxClassSecondLevel`;
//运营-资讯管理-资讯相关推荐
//已推荐列表
export const informationRecommendPageUrl = `${base}/cms/informationrecommend/page`;
//添加推荐列表
export const informationAddRecommendListUrl = `${base}/cms/informationrecommend/addRecommendList`;
//新增相关推荐
export const saveInformationRecommendUrl = `${base}/cms/informationrecommend/save`;
//移除推荐
export const deleteInformationRecommendUrl = `${base}/cms/informationrecommend/delete`;
//运营-资讯管理-资讯评论
//评论列表
export const informationCommentPageUrl = `${base}/cms/informationcomment/page`;
//删除评论
export const deleteInformationCommentUrl = `${base}/cms/informationcomment/delete`;
//评论状态修改
export const statusUpdateInformationCommentUrl = `${base}/cms/informationcomment/statusUpdate`;
//运营-资讯管理-审核
//审核信息
export const informationAuditInfoUrl = `${base}/cms/informationaudit`;
//提交审核
export const saveInformationAuditUrl = `${base}/cms/informationaudit/save`;

//运营-圈子管理-圈子分类
// 列表
export const circleClassUrl = `${base}/cms/circleclass/page`;
// 信息
export const circleClassInfoUrl = `${base}/cms/circleclass`;
// 新增
export const saveCircleClassUrl = `${base}/cms/circleclass/save`;
// 修改
export const updateCircleClassUrl = `${base}/cms/circleclass/update`;
// 删除
export const deleteCircleClassUrl = `${base}/cms/circleclass/delete`;
//圈子分类列表
export const circleClassListUrl = `${base}/cms/circleclass/circleClassList`;
//运营-圈子管理-圈子话题
// 列表
export const circleTopicPageUrl = `${base}/cms/circle/page`;
// 信息
export const circleTopicInfoUrl = `${base}/cms/circle`;
// 新增
export const saveCircleTopicUrl = `${base}/cms/circle/save`;
// 修改
export const updateCircleTopicUrl = `${base}/cms/circle/update`;
//删除
export const deleteCircleTopicUrl = `${base}/cms/circle/delete`;
//运营-圈子管理-帖子管理
// 列表
export const circleArticlePageUrl = `${base}/cms/circlearticle/page`;
// 信息
export const circleArticleInfoUrl = `${base}/cms/circlearticle`;
// 修改
export const updateCircleArticleUrl = `${base}/cms/circlearticle/update`;
//删除
export const deleteCircleArticleUrl = `${base}/cms/circlearticle/delete`;
// 文章状态修改
export const statusUpdateCircleUrl = `${base}/cms/circlearticle/statusUpdate`;
//运营-圈子管理-圈子评论
//评论列表
export const circleCommentPageUrl = `${base}/cms/circlecomment/page`;
//删除评论
export const deleteCircleCommentUrl = `${base}/cms/circlecomment/delete`;
//评论状态修改
export const statusUpdateCircleCommentUrl = `${base}/cms/circlecomment/statusUpdate`;
//运营-资讯管理-审核
//审核信息
export const circleAuditInfoUrl = `${base}/cms/circleaudit`;
//提交审核
export const saveCircleAuditUrl = `${base}/cms/circleaudit/save`;

//举报分页查询
export const circleReportPageUrl = `${base}/cms/circleReport/page`;
//举报修改状态
export const statusUpdateCircleReportUrl = `${base}/cms/circleReport/statusUpdate`;
//删除
export const deleteCircleReportUrl = `${base}/cms/circleReport/delete`;
//查看详情
export const circleReporInfoUrl = `${base}/cms/circleReport`;
// 删除地区
export const areaDel = `${base}/area`;
//禁用词删除
export const advertisingban = `${base}/stopword`;
