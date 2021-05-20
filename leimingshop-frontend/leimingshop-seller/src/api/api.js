import http from '@/utils/request'


let base = "/seller-api";
let requestType = { headers: { 'content-type': 'application/x-www-form-urlencoded' } };
// 上传图片--------------------------------------------------------------------------------------------------------------------
//Base64上传图片
export const uploadPicBase64 = (params, progressCb) => { return http.post(`${base}/picture/base64?type=${params.type}`, params, {onUploadProgress:progressCb}).then(res => res.data); };
//  上传文件
export const uploadFile = (params, progressCb) => { return http.post(`${base}/picture/video`, params, { onUploadProgress: progressCb }).then(res => res.data); };

// 属性管理--------------------------------------------------------------------------------------------------------------------
//  列表模块(主要是下拉用)
export const attributeList = params => { return http.get(`${base}/attribute/page`, params, requestType).then(res => res.data); };

//添加属性
export const addAttribute = params => { return http.post(`${base}/attribute`, params).then(res => res.data); };
// 编辑回显
export const backScanAttribute = params => { return http.get(`${base}/attribute/${params.id}`).then(res => res.data); };
//编辑属性
export const updateAttribute = params => { return http.put(`${base}/attribute`, params).then(res => res.data); };

// 分组模块
//添加属性分组
export const addAttributegroup = params => { return http.post(`${base}/attributegroup`, params).then(res => res.data); };
// 编辑回显
export const backScanAttributegroup = params => { return http.get(`${base}/attributegroup/${params.id}`).then(res => res.data); };
// 编辑属性分组
export const updateAttributegroup = params => { return http.put(`${base}/attributegroup`, params).then(res => res.data); };

// 启用禁用属性分组
export const statusAttributegroup = params => { return http.post(`${base}/attributegroup/status`, params, requestType).then(res => res.data); };
///结算---------------------------------------------------------------------
//结算详情
export const getbillInfo = params => { return http.get(`${base}/orderbill/${params.id}`, params).then(res => res.data); };
//订单结算列表
export const getOrderBillPage = params => { return http.get(`${base}/orderbill/order/page`, params).then(res => res.data); };
// 退款订单列表
export const getReturnBillPage = params => { return http.get(`${base}/orderbill/return/page`, params).then(res => res.data); };
// 结算日志
export const getBillLog = params => { return http.get(`${base}/orderbill/log`, params).then(res => res.data); };
//结算
export const bill = params => { return http.put(`${base}/orderbill`, params).then(res => res.data); };
//商家备注
export const storeRremark = params => { return http.put(`${base}/orderbill`, params).then(res => res.data); };

//商品分类 --------------------------------------------------------------------------------------------------------------------
//  商品分类--------------------------------------------------------------------------------------------------------------------
// 商品分类级联查询
export const getGoodscalss = params => { return http.get(`${base}/goodsclass/child/${params.id}`, params).then(res => res.data); };
// 查询所有分类(分层级关系
export const allGoodsclass = params => { return http.get(`${base}/goodsclass/all/goodclass`, params).then(res => res.data); };
// 查询树形结构
export const allTreeGoodsclass = params => { return http.get(`${base}/goodsclass/tree/goods/class`, params).then(res => res.data); };


// 根据id查询分类所有信息
export const goodsclassById = params => { return http.get(`${base}/goodsclass/${params.params.gcId}`, params).then(res => res.data); }
//新建
export const addGoodsclass = params => { return http.post(`${base}/goodsclass`, params).then(res => res.data); }
//编辑
export const updataGoodsclass = params => { return http.put(`${base}/goodsclass`, params).then(res => res.data); }
// 根据分类id查询品牌信息
export const goodsclassBrandById = params => { return http.get(`${base}/goodsclass/brand/id`,params).then(res => res.data); }

// 查询全部一级分类/根据父级分类查出子级分类
// export const goodsclassPage = params => { return http.get(`${base}/goodsclass/page`,params).then(res => res.data); }

// 商品展示类目管理--------------------------------------------------------------------------------------------------------------------
//类目列表
// export const backstageList = params => { return http.get(`${base}/goods/classcustom/all`).then(res => res.data); }
//新建
export const addGoodsclasscustom = params => { return http.post(`${base}/goods/classcustom`, params).then(res => res.data); }
//编辑
export const updataGoodsclasscustom = params => { return http.put(`${base}/goods/classcustom`, params).then(res => res.data); }
// 根据id查询分类所有信息
export const goodsclasscustomById = params => { return http.get(`${base}/goods/classcustom/${params.params.gcId}`, params).then(res => res.data); }
//   启用/禁用展示类目
export const goodsclasscustomUpdateShow = params => { return http.get(`${base}/goods/classcustom/update/show`, params).then(res => res.data); }



// 查询规格分组及规格列表
// export const attributegroupListSpec = params => { return http.get(`${base}/attributegroup/list/spec`, params,requestType).then(res => res.data); };
// -----------------------------------------------------------------------------------------------------------------
export const goodsTime = params => { return http.get(`${base}/goods/show/timing/${params.id}`).then(res => res.data); }
// 商品标签--------------------------------------------------------------------------------------------------------------------
export const tagAllList = params => { return http.get(`${base}/label/list`, params).then(res => res.data); };

// 商品管理--------------------------------------------------------------------------------------------------------------------
//添加商品
export const addGoods = params => { return http.post(`${base}/goods`, params).then(res => res.data); };
// 编辑商品，商品修改
export const updateGoods = params => { return http.put(`${base}/goods`, params).then(res => res.data); };
// 编辑回显
export const backScanGoods = params => { return http.get(`${base}/goods/${params.id}`).then(res => res.data); }
// 编辑回显
export const backScanDetailGoods = params => { return http.get(`${base}/goods/detail/${params.id}`).then(res => res.data); }
// 商品上下架状态批量修改
export const showBatchGoods = params => { return http.put(`${base}/goods/show`, params).then(res => res.data); };
//商品定时上下架修改
export const timedShowBatchGoods = params => { return http.put(`${base}/goods/timed/show`, params).then(res => res.data); };
// 单个商品上下架状态修改
// export const showGoods = params => { return http.put(`${base}/goods/show/${params.id}`, params).then(res => res.data); };
// 验证规格货号有没有重复
export const validGoodsSeria = params => { return http.post(`${base}/goods/valid/goodsSerial`, params).then(res => res.data); };


// 商品审核--------------------------------------
//修改商品审核状态
export const checkGoods = params => { return http.put(`${base}/goods/check`, params).then(res => res.data); };
//批量修改商品审核状态
export const checkBatchGoods = params => { return http.put(`${base}/goods/check/batch?goodsStatus=${params.goodsStatus}&rejectReason=${params.rejectReason}`, params.ids).then(res => res.data); };


// 商品规格管理------------------------------------------------------------------------------------------------------
export const goodsSpecTime = params => { return http.get(`${base}/goods/spec/show/timing/${params.id}`).then(res => res.data); }
// 分页查询商品SKU列表
// export const goodsSpecPage = params => { return http.post(`${base}/goods/spec/page`, params).then(res => res.data); };
// 商品规格上下架状态批量修改
export const goodsSpecShow = params => { return http.put(`${base}/goods/spec/show`, params).then(res => res.data); };
// 商品规格定时上下架状态批量修改
export const timedGoodsSpecShow = params => { return http.put(`${base}/goods/spec/timed/show`, params).then(res => res.data); };
// 商品规格回显
export const backScangoodsSpec = params => { return http.get(`${base}/goods/spec/${params.id}`).then(res => res.data); };
// 修改商品sku
export const updateGoodsSpecSku = params => { return http.put(`${base}/goods/spec/sku`, params).then(res => res.data); };

// 价格管理 价格修改记录管理--------------------------------------------------------------------------------------------------------------------
// 修改价格记录
export const updatePrice = params => { return http.post(`${base}/price/log`, params).then(res => res.data); };
// 根据ID获取信息
export const backScanPrice = params => { return http.get(`${base}/price/log/${params.id}`, params).then(res => res.data); };


// PUT /goods/spec/update/price
// 根据ID获取价格和库存信息
export const backScanPriceAndStorage = params => { return http.get(`${base}/goods/spec/price/storage/${params.id}`, params).then(res => res.data); };
// 批量修改价格
export const uptateprice = params => { return http.put(`${base}/goods/spec/update/price`, params).then(res => res.data); };
//批量修改库存
export const updataStorage = params => { return http.put(`${base}/goods/spec/update/storage`, params).then(res => res.data); };




// 库存管理 库存修改记录管理---------------------------------------------------------------------------------------------------------------------
// 修库存记录
export const updateStock = params => { return http.post(`${base}/stock/log`, params).then(res => res.data); };
// 根据ID获取信息
export const backScanStock = params => { return http.get(`${base}/stock/log/${params.id}`, params).then(res => res.data); };

// 售后模板---------------------------------------------------------------------------------------------------
//  列表模块(主要是下拉用)
export const aftertemplateList = params => { return http.get(`${base}/aftertemplate/list`, params).then(res => res.data); };
//保存售后模板
export const addAftertemplate = params => { return http.post(`${base}/aftertemplate`, params).then(res => res.data); };
//修改售后模板
export const updateAftertemplate = params => { return http.put(`${base}/aftertemplate`, params).then(res => res.data); };
// 根据id查询售后模板
export const backScanAftertemplate = params => { return http.get(`${base}/aftertemplate/${params.id}`, params).then(res => res.data); };

// 规格管理-------------------------------------------------------------------------------------------------------------------------------------
//  列表模块(主要是下拉用)
export const specList = params => { return http.get(`${base}/spec/page`, params, requestType).then(res => res.data); };

//添加规格
export const addSpec = params => { return http.post(`${base}/spec`, params).then(res => res.data); };
// 编辑回显
export const backScanSpec = params => { return http.get(`${base}/spec/${params.id}`).then(res => res.data); }
//编辑规格
export const updateSpec = params => { return http.put(`${base}/spec`, params).then(res => res.data); };

//添加规格分组
export const addSpecgroup = params => { return http.post(`${base}/specgroup`, params).then(res => res.data); };
// 编辑回显
export const backScanSpecgroup = params => { return http.get(`${base}/specgroup/${params.id}`).then(res => res.data); }
// 编辑规格分组
export const updateSpecgroup = params => { return http.put(`${base}/specgroup`, params).then(res => res.data); };

// 启用禁用规格分组
export const statusSpecgroup = params => { return http.post(`${base}//specgroup/status`, params, requestType).then(res => res.data); };



// 品牌管理--------------------------------------------------------------------------------------------------------------------
// 品牌列表（主要用于下拉）
// 编辑回显   不带分页
export const brandListPull = params => { return http.get(`${base}/brand/list`, params).then(res => res.data); }
// 品牌列表 带分页
export const brandList = params => { return http.get(`${base}/brand/page`, params).then(res => res.data); }
//添加品牌
export const addBrand = params => { return http.post(`${base}/brand`, params).then(res => res.data); };
// 编辑回显
export const backScanBrand = params => { return http.get(`${base}/brand/${params.id}`).then(res => res.data); }
//编辑品牌
export const updateBrand = params => { return http.put(`${base}/brand`, params).then(res => res.data); };

// 商品评价-------------------------------------------------------------------------------------------------------------------
// /评价详情
export const evaDets = params => { return http.get(`${base}/evaluate/${params.id}`, params).then(res => res.data); }
//商品评价--回复
export const msgReply = params => { return http.put(`${base}/evaluate/reply`, params).then(res => res.data); }
//商品评价--显示/隐藏
export const changeStatus = params => { return http.put(`${base}/evaluate/batch`, params).then(res => res.data); }

//商品评价 --批量修改读取状态
export const changeReadStatus = params => { return http.put(`${base}/evaluate/batch/read/state`, params).then(res => res.data); }

//店铺设置
//店铺信息回显
export const storeNews = params => { return http.get(`${base}/setting/echo`).then(res => res.data); }
// 修改店信息
export const updateStore = params => { return http.put(`${base}/setting/update`, params).then(res => res.data); };
// 修改入驻信息信息
export const settingUpdateStore = params => { return http.put(`${base}/setting/update/store`, params).then(res => res.data); };
// 管理员重置密码
export const resetPassword = params => { return http.put(`${base}/user/reset/password?id=${params.id}`, params).then(res => res.data); }
// 创建店铺
export const addStore = params => { return http.post(`${base}/setting/save`,params).then(res => res.data); }
// 店铺详情
export const storeInfo = params => { return http.get(`${base}/setting/info`,params).then(res => res.data); }
//店铺首页
export const storeIndex = params => { return http.get(`${base}/setting/index`).then(res => res.data); }
//获取店铺商品实况
export const storegoodsLive = params => { return http.get(`${base}/setting/goods?type=${params.type}`,params).then(res => res.data); }
//获取店铺订单实况
export const storeorderLive = params => { return http.get(`${base}/setting/orderlive?type=${params.type}`,params).then(res => res.data); }
// 店铺列表
export const storeList = params => { return http.get(`${base}/setting/store/list`,params).then(res => res.data); }
// 提交审核
export const storeUdateStatus = params => { return http.put(`${base}/setting/update/status`,params).then(res => res.data); }
// 启用禁用用户
export const isEnable = params => { return http.put(`${base}/user/isenable?userId=${params.id}&type=${params.type}`,params).then(res => res.data); }
// 店铺基础信息回显
export const storebBasicInfo= params => { return http.get(`${base}/setting/basic/info`,params).then(res => res.data); }
// 修改店铺基础信息
export const storeUdateBasicInfo = params => { return http.put(`${base}/setting/update/info`,params).then(res => res.data); }

// 店铺公司信息回显
export const storebCompanyInfo= params => { return http.get(`${base}/setting/company/info`,params).then(res => res.data); }
// 修改店铺基础信息
export const storeUdateCompanyInfo = params => { return http.put(`${base}/setting/update/auth`,params).then(res => res.data); }
// 保存用户配置的首页功能按钮
export const saveUserFn= params => { return http.post(`${base}/storeuserfunction`, params).then(res => res.data); };
// 订单支付有效时间
export const getOrderCancelTime= params => { return http.get(`${base}/sys/setting/order/setting`, params).then(res => res.data); };


//订单管理----------------------------------------------------------------------------------06-25
//订单列表--根据id查询详情
export const orderDetail = params => { return http.get(`${base}/order/${params.id}`, params).then(res => res.data); }
//订单列表--根据订单编号查询详情
export const orderDetailBybh = params => { return http.get(`${base}/order/orderSn/${params.orderSn}`, params).then(res => res.data); }



//订单明细
export const toOrderSnDetail = params => { return http.get(`${base}/order/activity/detail/orderSn/${params.orderSn}`, params).then(res => res.data); }

//增加商家备注
export const sellerRemarkInfo = params => { return http.put(`${base}/order/seller/remark`, params).then(res => res.data); }
//退货详情--根据订单编号
export const returnDetail = params => { return http.get(`${base}/aftersale/return/detail/${params.aftersaleSn}`, params).then(res => res.data); }
//换货详情
export const exchDetail = params => { return http.get(`${base}/aftersale/barter/detail/${params.aftersaleSn}`, params).then(res => res.data); }
// 售后审核详情
export const salesAuditDetail = params => { return http.get(`${base}/aftersale/audit/detail/${params.aftersaleSn}`, params).then(res => res.data); }
//退换货审核详情
export const salesDetail = params => { return http.get(`${base}/aftersale/apply/detail/${params.aftersaleSn}`, params).then(res => res.data); }
//售后审核
export const examineGoods = params => { return http.post(`${base}/aftersale/apply/reason`, params).then(res => res.data); }
//仲裁管理
export const arbitrationPage = params => { return http.get(`${base}/aftersale/arbitration/page`, params).then(res => res.data); }
//仲裁详情
export const arbitrationDetail = params => { return http.get(`${base}/arbitration/detail/${params.id}`, params).then(res => res.data); }
//仲裁审核
export const arbitrationAudit = params => { return http.put(`${base}/arbitration/audit`, params).then(res => res.data); }
//支付方式
export const paymentList = params => { return http.get(`${base}/payment/list`, params).then(res => res.data); }
//换货确认入库
export const sureExchangeGoods = params => { return http.post(`${base}/aftersale/barter/confirm?aftersaleSn=${params.aftersaleSn}&aftersaleStatus=${params.aftersaleStatus}`, params).then(res => res.data); }
//退货确认入库
export const sureReturnGoods = params => { return http.post(`${base}/aftersale/return/confirm?aftersaleSn=${params.aftersaleSn}&aftersaleStatus=${params.aftersaleStatus}`, params).then(res => res.data); }
//退货退款
export const sureReturnMoney = params => { return http.put(`${base}/refund/wx/refund?id=${params.id}`, params).then(res => res.data); }
//发货
export const delGoodsLog = params => { return http.put(`${base}/order/shippment`, params).then(res => res.data); }
//物流公司
export const logCompany = params => { return http.get(`${base}/logistics/company`, params).then(res => res.data); }
//换货提交物流记录
export const exGoodsLog= params => { return http.post(`${base}/aftersale/barter/express?aftersaleSn=${params.aftersaleSn}&sellerDeliveryType=${params.sellerDeliveryType}&sellerLogisticsCompany=${params.sellerLogisticsCompany}&sellerLogisticsNumber=${params.sellerLogisticsNumber}
`, params).then(res => res.data); }

//取消订单
export const cancelOrder = params => { return http.put(`${base}/order/cancel`, params).then(res => res.data); }
// 原因接口，下拉列表
export const orderReasonList = params => { return http.get(`${base}/order/apply/reason`, params).then(res => res.data); }



// 广告管理--------------------------------------------------------------------------------------------------------------------
//普通广告新增
export const plainAdveAdd = params => { return http.post(`${base}/adv`, params).then(res => res.data); };
//普通广告类别下拉
export const plainAdveSlect = params => { return http.get(`${base}/adv/category/list/enabled`).then(res => res.data); }
//普通广告修改
export const plainAdveUpdate = params => { return http.put(`${base}/adv`, params).then(res => res.data); };
//广告详情
export const adverDetail = params => { return http.get(`${base}/adv/${params.id}`).then(res => res.data); }
//移动楼层回显
export const flooradveDetail = params => { return http.get(`${base}/webfloor/${params.id}`).then(res => res.data); }
//修改移动楼层
export const updateMobbileFloor = params => { return http.put(`${base}/webfloor`, params).then(res => res.data); };
//新增移动楼层
export const addMobbileFloor = params => { return http.post(`${base}/webfloor`, params).then(res => res.data); };
//移动楼层标识查重
export const mobbileFloorCnki = params => { return http.get(`${base}/webfloor/check/code`, params).then(res => res.data); }
//移动楼层中的商品列表
export const mobbileGoodsList = params => { return http.get(`${base}/goods/page`, params).then(res => res.data); }
//移动楼层中的商品分类联查列表
export const mobbileClassGoodsList = params => { return http.get(`${base}/goods/classcustom/child/show/${params.id}`).then(res => res.data); }
// 保存pc首页导航
export const addNavigation = params => { return http.post(`${base}/navigation`, params).then(res => res.data); };
//修改pc首页导航
export const updateNavigation = params => { return http.put(`${base}/navigation`, params).then(res => res.data); };
//导航详情
export const navigationDetail = params => { return http.get(`${base}/navigation/${params.id}`).then(res => res.data); }


//楼层展示分类
export const floorclassList = params => { return http.get(`${base}/goods/classcustom/custom/tree`).then(res => res.data); }

//楼层，分类广告新增
export const floorClassAdveAdd = params => { return http.post(`${base}/adv/special`, params).then(res => res.data); };
//楼层广告类别下拉
export const floorAdveSlect = params => { return http.get(`${base}/webfloor/list/enabled`,params).then(res => res.data); }
//分类广告类别下拉
// export const classAdveSlect = params => { return http.get(`${base}/goods/classcustom/child/show/${params.id}`).then(res => res.data); }
// 店铺商品分类
export const storeGoodsclass = params => { return http.get(`${base}/storegoodsclass/list`, params).then(res => res.data); };
// 查询店铺商品所有一级分类
export const firstClass = params => { return http.get(`${base}/storegoodsclass/first/class?type=${params.type}`, params).then(res => res.data); };
//新增店铺商品分类
export const addstoreGoodsclasscustom = params => { return http.post(`${base}/storegoodsclass`, params).then(res => res.data); }
//编辑店铺商品分类
export const updatastoreGoodsclasscustom = params => { return http.put(`${base}/storegoodsclass`, params).then(res => res.data); }
// 根据id查询店铺商品分类
export const storeGoodsClassInfo = params => { return http.get(`${base}/storegoodsclass/${params.params.gcId}`, params).then(res => res.data); }
//   启用/禁用店铺商品分类
export const storeGoodsclasscustomUpdateShow = params => { return http.get(`${base}/storegoodsclass/update/show`, params).then(res => res.data); }

// 图片--------------------------------------------------------------------------------------------------------------------
//获取图片分组列表
export const getpicturecategoryallapi = params => { return http.get(`${base}/picture/category/all`, params).then(res => res.data); }
//新增一个分组列表
export const addpicturecategory = params => { return http.post(`${base}/picture/category`, params).then(res => res.data); }
//图片分组的删除
export const deleteImage = params => { return http.delete(`${base}/picture/category`, params).then(res => res.data);}
//修改图片分组名称
export const updataPictureCategory = params => { return http.put(`${base}/picture/category`, params).then(res => res.data); }
//删除图片
export const deletepicture = params => { return http.delete(`${base}/picture`, params).then(res => res.data);}
//批量保存图片  目前不用
export const saveimagesurl = params => { return http.post(`${base}/picture`, params).then(res => res.data); }

/*店铺系列*/
// 手机号验重
export const verifyMobile = params => { return http.get(`${base}/register/verify/mobile?mobile=${params.mobile}`, params).then(res => res.data); }
// 获取手机验证码
export const getMobileCode = params => { return http.get(`${base}/register/mobile/code`, params).then(res => res.data); }
// 检验用户名是否重复
export const verifyUserName = params => { return http.get(`${base}/register/verify/username?userName=${params.userName}`, params).then(res => res.data); }
// 注册账号
export const registerShops = params => { return http.post(`${base}/register`, params).then(res => res.data); }
// 验证码是否一致
export const isSameCode = params => { return http.get(`${base}/register/verify/mobile/code`, params).then(res => res.data); }
// 检验店铺名称是否重复
export const verifyStoreName = params => { return http.get(`${base}/register/verify/store/name?storeName=${params.storeName}&storeId=${params.storeId}`,params).then(res => res.data); }
//获取用户协议
export const noticeProtocol = params => { return http.get(`${base}/register/notice/protocol`, params).then(res => res.data); }

/*优惠券系列*/
/*新增优惠券*/
export const addCoupons = params => { return http.post(`${base}/con/coupons/activity`, params).then(res => res.data); }
/*编辑优惠券*/
export const editCommit = params => { return http.put(`${base}/con/coupons/activity`, params).then(res => res.data); }
/*编辑获取数据*/
export const editCoupons = params => { return http.get(`${base}/con/coupons/activity/${params.id}?type=${params.type}`, params).then(res => res.data); }
/*停止优惠券活动*/
export const stopCoupons = params => { return http.put(`${base}/con/coupons/activity/stop/${params.id}`, params).then(res => res.data); }
/*商品列表*/
export const getGoodsPage = params => { return http.get(`${base}/goods/page`, params).then(res => res.data); }
/*店铺品牌列表*/
export const storeBrandList = params => { return http.get(`${base}/brand/store/list`, params).then(res => res.data); }


/*拼团系列*/
/*新增拼团活动列表*/
export const addGroup = params => { return http.post(`${base}/group`, params).then(res => res.data); }
/*编辑拼团活动*/
export const editGroup = params => { return http.put(`${base}/group`, params).then(res => res.data); }
/*拼团活动详情*/
export const groupDetails = params => { return http.get(`${base}/group/${params.id}`, params).then(res => res.data); }
/*添加拼团活动商品 -- 获取商品的规格*/
export const getGroupListSku = params => { return http.get(`${base}/group/list/sku/add?activityId=${params.activityId}&goodsId=${params.goodsId}`, params).then(res => res.data); }
/*添加拼团活动商品 -- 保存接口*/
export const getGroupGoodsAdd = params => { return http.put(`${base}/group/goods/add`, params).then(res => res.data); }
/*删除拼团活动商品 */
export const deletetGroupGoods = params => { return http.delete(`${base}/group/goods`, {data:params}).then(res => res.data); }
/*查看拼团记录详情 */
export const getGroupRecordDetails = params => { return http.get(`${base}/group/record/detail/${params.id}`, params).then(res => res.data); }
/*拼团活动-快速成团 */
export const groupComposition = params => { return http.put(`${base}/group/composition/${params.groupRecordId}`, params).then(res => res.data); }
/*拼团活动-取消订单 */
export const groupOrderCancel = params => { return http.put(`${base}/group/order/cancel/${params.groupRecordId}/${params.reasonId}`, params).then(res => res.data); }
/*拼团活动-获取订单默认设置 */
export const getOrderChangeDetailApi = params => { return http.get(`${base}/group/order/change/detail`, params).then(res => res.data); }
/*拼团活动-停止拼团活动 */
export const stopGroupActivity = params => { return http.put(`${base}/group/activity/stop/${params.id}`, params).then(res => res.data); }

/*虚拟订单系列*/
/*电子提货码详情*/
export const fetchCodeDetail = params => { return http.get(`${base}/order/fetchcode/detail`, params).then(res => res.data); }
/*核销记录详情*/
export const verifyCodeDetail = params => { return http.get(`${base}/order/verify/detail/${params.id}`, params).then(res => res.data); }
/*确认核销*/
export const orderVerify = params => { return http.put(`${base}/order/verify`, params).then(res => res.data); }

//商品统计分页查询
export const goodsStatisPage = params => { return http.get(`${base}/statis/goods/goods/sale/statis`, params).then(res => res.data); }

//交易统计
export const transaction = params => { return http.get(`${base}/statis/order/find/transaction`, params).then(res => res.data); }
export const seven = params => { return http.get(`${base}/statis/order/seven/day/statis`, params).then(res => res.data); }
export const single = params => { return http.get(`${base}/statis/order/single/consumer`, params).then(res => res.data); }

//商品统计
export const goodstatis = params => { return http.get(`${base}/statis/goods/class/goods/statis`, params).then(res => res.data); }
export const salestatis = params => { return http.get(`${base}/statis/goods/goods/sale/statis`, params).then(res => res.data); }
// 订单来源数据展示
export const staticOrderSource = params => { return http.get(`${base}/statis/order/source`, params).then(res => res.data); }

//营销统计
export const activity = params => { return http.get(`${base}/activity`, params).then(res => res.data); }
export const activityDetail = params => { return http.get(`${base}/activity/detail`, params).then(res => res.data); }

/*编辑商品活动校验*/
export const goodsUpdateCheckActivity = params => { return http.post(`${base}/seckill/check/goods/activity`, params).then(res => res.data); }

/*满减活动系列*/
/*新增满减活动*/
export const addFullReduction = params => { return http.post(`${base}/reduce/activity`, params).then(res => res.data); }
/*停止满减活动*/
export const stopFullReduction = params => { return http.put(`${base}/reduce/activity/stop/${params.id}`, params).then(res => res.data); }
/*编辑满减活动*/
export const editCommitFullReduction = params => { return http.put(`${base}/reduce/activity`, params).then(res => res.data); }
/*编辑获取数据*/
export const editFullReduction = params => { return http.get(`${base}/reduce/activity/${params.id}?type=${params.type}`, params).then(res => res.data); }

/*可添加活动商品商品分页*/
export const canAddActiveGoods = params => { return http.delete(`${base}/activity/goods/list`, params).then(res => res.data); }
/*秒杀活动系列*/
/*新增秒杀活动*/
export const saveSeckillActivity = params => { return http.post(`${base}/seckill/activity`, params).then(res => res.data); }
/*编辑秒杀活动*/
export const editSeckillActivity = params => { return http.put(`${base}/seckill/activity`, params).then(res => res.data); }
/*删除秒杀活动*/
export const deleteSeckillActivity = params => { return http.delete(`${base}/seckill/activity`, params).then(res => res.data); }
/*停止秒杀活动*/
export const stopSeckillActivity = params => { return http.put(`${base}/seckill/activity/stop/${params.id}`, params).then(res => res.data); }
/*查询秒杀活动详情*/
export const seckillActivityDetail = params => { return http.get(`${base}/seckill/activity/${params.id}`, params).then(res => res.data); }
/*查询秒杀活动场次列表*/
export const seckillSessionList = params => { return http.get(`${base}/seckill/session/list`, params).then(res => res.data); }
/*秒杀商品列表*/
export const seckillGoodsPage = params => { return http.get(`${base}/seckill/goods/list`, params).then(res => res.data); }
/*秒杀商品sku活动数据回显*/
export const seckillGoodsSkuDetail = params => { return http.get(`${base}/seckill/goods/sku/add/list`, params).then(res => res.data); }
/*保存编辑秒杀商品*/
export const saveSeckillGoods = params => { return http.put(`${base}/seckill/goods/add`, params).then(res => res.data); }
/*删除秒杀商品*/
export const deletSeckillGoods = params => { return http.delete(`${base}/seckill/goods`, params).then(res => res.data); }
/*添加秒杀商品分页*/
export const addSeckillGoodsPage = params => { return http.delete(`${base}/seckill/goods/add/list`, params).then(res => res.data); }
/*获取秒杀设置*/
export const getSekillSetting = params => { return http.get(`${base}/seckill/setting`, params).then(res => res.data); }
/*限时购功能 */
/*保存限时购活动 */
export const saveFlashSaleActivity = params => { return http.post(`${base}/flash/sale`, params).then(res => res.data); }
/*编辑限时购活动*/
export const editFlashSaleActivity = params => { return http.put(`${base}/flash/sale`, params).then(res => res.data); }
/*保存编辑秒杀商品*/
export const saveFlashSaleGoods = params => { return http.put(`${base}/flash/sale/goods/add`, params).then(res => res.data); }
/*查询限时抢购活动详情*/
export const flashSaleActivityDetail = params => { return http.get(`${base}/flash/sale/${params.id}`, params).then(res => res.data); }
/*删除限时抢购商品*/
export const deleteFlashSaleGoods = params => { return http.delete(`${base}/flash/sale/goods`, params).then(res => res.data); }
/*停止限时抢购活动*/
export const stopFlashSaleActivity = params => { return http.put(`${base}/flash/sale/stop/${params.id}`, params).then(res => res.data); }
// 运费模板
/*查询运费规则设置*/
export const getFreightRuleSetting = params => { return http.get(`${base}/freight/rule`, params).then(res => res.data); }
/*修改运费规则设置*/
export const editFreightRuleSetting = params => { return http.put(`${base}/freight/rule/edit`, params).then(res => res.data); }
/*店铺运费模板列表*/
export const storeFreightTemplateList = params => { return http.get(`${base}/freight/template/list`, params).then(res => res.data); }
/*店铺运费模板分页*/
export const freightTemplatePage = params => { return http.get(`${base}/freight/template/admin/page`, params).then(res => res.data); }
/*设置默认运费模板*/
export const setDefaultFreightTemplate = params => { return http.put(`${base}/freight/template/default?id=${params.id}&defaultFlag=${params.defaultFlag}`, params).then(res => res.data); }
/*删除运费模板*/
export const deleteFreightTemplate = params => { return http.delete(`${base}/freight/template`, params).then(res => res.data); }
/*计算运费模板关联商品数量*/
export const countFreightTemplateGoods = params => { return http.get(`${base}/freight/template/count/goods?id=${params.id}`, params).then(res => res.data); }
/*删除并更新商品运费模板*/
export const deleteAndUpdateFreightTemplate = params => { return http.delete(`${base}/freight/template/goods/template?oldFreightTemplateId=${params.oldFreightTemplateId}&newFreightTemplateId=${params.newFreightTemplateId}`, params).then(res => res.data); }
/*运费模板详情*/
export const freightTemplateDetail = params => { return http.get(`${base}/freight/template/admin/detail?id=${params.id}`, params).then(res => res.data); }
/*运费模板保存*/
export const saveFreightTemplate = params => { return http.post(`${base}/freight/template/add`, params).then(res => res.data); }
/*运费模板编辑*/
export const updateFreightTemplate = params => { return http.put(`${base}/freight/template/edit`, params).then(res => res.data); }

/*大区及地区分层地址*/
export const redionAndAreaTree = params => { return http.get(`${base}/area/region/area/tree`, params).then(res => res.data); }

/*登录*/
// 校验账号是否存在
export const verifyUsername = params => { return http.get(`${base}/verify/username`, params).then(res => res.data); }


// 获取图片验证码
export const generateImage = params => { return http.get(`${base}/generate/image`, params).then(res => res.data); }
// 校验图形验证码
export const verifyCaptcha = params => { return http.get(`${base}/verify/captcha`, params).then(res => res.data); }

// 获取手机或邮箱验证
export const phoneCaptcha = params => { return http.get(`${base}/phone/captcha`, params).then(res => res.data); }
// 校验手机号邮箱验证码是否正确
export const verifyPhoneCaptcha = params => { return http.get(`${base}/verify/phone/captcha`, params).then(res => res.data); }


// 修改密码
export const forgetPassword = params => { return http.put(`${base}/forget/password`, params).then(res => res.data); }
// 站内信列表
export const messageList = params => { return http.get(`${base}/message/text/list`, params).then(res => res.data); }
// 站内信列表
export const messagePage = params => { return http.get(`${base}/message/text/page`, params).then(res => res.data); }
//删除消息
export const deletemessage = params => { return http.delete(`${base}/message/text`, params).then(res => res.data);}
//站内信详情
export const messageInfo = params => { return http.get(`${base}/message/text/${params.id}`, params).then(res => res.data); }
// 批量已读
export const batchRead = params => { return http.put(`${base}/message/text/batch/read`, params).then(res => res.data); }

/*会员等级列表*/
export const memberGradeList = params => { return http.get(`${base}/membergrade/list`, params).then(res => res.data); }


// 库存预警管
// 信息
export const storagewarningInfo = params => { return http.get(`${base}/storagewarning`, params).then(res => res.data); }
// 保存
export const storagewarningSave = params => { return http.post(`${base}/storagewarning`, params).then(res => res.data); }
// 修改
export const messageInfoEdit = params => { return http.put(`${base}/storagewarning`, params).then(res => res.data); }

//首页个人信息(回显)
export const personnal = params => { return http.get(`${base}/user/person/info/id`, params).then(res => res.data); }

//更换头像
export const editLogo = params => { return http.put(`${base}/user/person/info/logo`, params).then(res => res.data);}

//修改商户手机号
export const editPhone = params => { return http.put(`${base}/user/person/info/phone`, params).then(res => res.data);}

//更换邮箱
export const editEmail = params => { return http.put(`${base}/user/person/info/email`, params).then(res => res.data);}

//修改密码
export const editPwd = params => { return http.put(`${base}/user/person/info/pwd`, params).then(res => res.data);}

// 导出管理
export const sysexportmanagementPage = params => { return http.get(`${base}/sysexportmanagement/page`, params).then(res => res.data); };


//获取发票控制详情
export const getStoreinvoiceDetail = params => { return http.get(`${base}/storeinvoice/detail`, params).then(res => res.data); };
//获取发票设置详情
export const getStoreInvoiceSetting = params => { return http.get(`${base}/storeinvoice/setting`, params).then(res => res.data); };
//保存发票控制详情
export const savaAndEditInvoice = params => { return http.post(`${base}/storeinvoice/change/setting`, params).then(res => res.data); };
//获取发票详情
export const getinvoiceinfo = params => { return http.get(`${base}/invoiceinfo/detail/${params.invoiceId}`, params).then(res => res.data); };
export const getinvoiceDetail = params => { return http.get(`${base}/orderinvoice/detail/${params.id}`, params).then(res => res.data); };
//开票接口
export const putMakeInvoice = params => { return http.put(`${base}/orderinvoice/make/invoice`, params).then(res => res.data); };
//上传发票接口
export const fileInvoiceUploadApi = params => { return http.post(`${base}/file/invoice/upload`, params).then(res => res.data); };
// 发送邮箱
export const sendEmail= params => { return http.put(`${base}/orderinvoice/send/mail?email=${params.email}&id=${params.id}`, params).then(res => res.data); };
