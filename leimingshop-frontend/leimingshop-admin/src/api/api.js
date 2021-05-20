import http from '@/utils/request'


let base = "/admin-api";
let requestType = { headers: { 'content-type': 'application/x-www-form-urlencoded' } };
// 上传图片--------------------------------------------------------------------------------------------------------------------
//Base64上传图片
export const uploadPicBase64 = (params, progressCb) => { return http.post(`${base}/picture/base64`, params, {onUploadProgress:progressCb}).then(res => res.data); };
//  上传文件
export const uploadFile = (params, progressCb) => { return http.post(`${base}/picture/video`, params, { onUploadProgress: progressCb }).then(res => res.data); };

// 修改底部Icon配置
export const iconEdit = params => { return http.put(`${base}/icon`, params).then(res => res.data); };

// 属性管理--------------------------------------------------------------------------------------------------------------------
//  列表模块(主要是下拉用)
export const attributeList = params => { return http.get(`${base}/attribute/page`, params, requestType).then(res => res.data); };

//添加属性
export const addAttribute = params => { return http.post(`${base}/attribute`, params).then(res => res.data); };
// 编辑回显
export const backScanAttribute = params => { return http.get(`${base}/attribute/${params.id}`).then(res => res.data); };
//编辑属性
export const updateAttribute = params => { return http.put(`${base}/attribute`, params).then(res => res.data); };
//查询属性是否关联后台分类
export const attributeCheckClasss = params => { return http.post(`${base}/attribute/check/class`, params).then(res => res.data); };

// 分组模块
//添加属性分组
export const addAttributegroup = params => { return http.post(`${base}/attributegroup`, params).then(res => res.data); };
// 编辑回显
export const backScanAttributegroup = params => { return http.get(`${base}/attributegroup/${params.id}`).then(res => res.data); };
// 编辑属性分组
export const updateAttributegroup = params => { return http.put(`${base}/attributegroup`, params).then(res => res.data); };

// 启用禁用属性分组
export const statusAttributegroup = params => { return http.post(`${base}/attributegroup/status`, params, requestType).then(res => res.data); };
// 商品规格删除前先看规格有没有关联分类
export const attributeCheckClass = params => { return http.get(`${base}/attribute/check/class/${params.id}`).then(res => res.data); };

//专题页新增
export const topicAdd = params => { return http.post(`${base}/topic`, params).then(res => res.data); };
//专题页编辑
export const topicUpdate = params => { return http.post(`${base}/topic/update`, params).then(res => res.data); }
//获取专题页信息
export const getTopic = params => { return http.get(`${base}/topic/page`, params).then(res => res.data); }
//获取所有专题页信息
export const getTopicList = params => { return http.get(`${base}/topic/all`, params).then(res => res.data); }
//获取专题页商品信息
export const getTopicGoods = params => { return http.get(`${base}/topic/goods/page`, params).then(res => res.data); }
//专题页信息保存
export const topicSave = params => { return http.post(`${base}/topic`, params).then(res => res.data); }
//专题页删除
export const deleteTopic = params => { return http.post(`${base}/topic/del?id=${params.id}`).then(res => res.data); }
//专题页详情
export const getTopicDetail = params => { return http.get(`${base}/topic/${params.id}`).then(res => res.data); }

//商品分类 --------------------------------------------------------------------------------------------------------------------
//  商品分类--------------------------------------------------------------------------------------------------------------------
// 商品分类级联查询
export const getGoodscalss = params => { return http.get(`${base}/goodsclass/child/${params.id}`, params).then(res => res.data); };
// 查询所有分类(分层级关系
export const allGoodsclass = params => { return http.get(`${base}/goodsclass/all/goodclass`, params).then(res => res.data); };
// 查询树形结构
// export const allTreeGoodsclass = params => { return http.get(`${base}/goodsclass/tree/goods/class`, params).then(res => res.data); };
export const allTreeGoodsclass = params => { return http.get(`${base}/goodsclass/all/goodclass`, params).then(res => res.data); };


//购物车修改
export const cartConfigSort = params => { return http.put(`${base}/cartrecom/update`, params).then(res => res.data); };
//购物车添加商品
export const addGoodscarList = params => { return http.post(`${base}/cartrecom/add`, params).then(res => res.data); };
//添加商品未推荐列表
export const goodsListVisible = params => { return http.get(`${base}/cartrecom/goods/page`, params).then(res => res.data); };
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


//消息-------------------------------------------------------------------------------------------------------------
//模板分页列表
export const getmessagepage = params => { return http.get(`${base}/template/page`, params).then(res => res.data); };
//修改模板列表状态
export const putMessageState = params => { return http.put(`${base}/template`, params).then(res => res.data); };
//消息列表删除
export const deleteMessage = params => { return http.delete(`${base}/template`, params).then(res => res.data); };
//接收人保存
export const saveMessage = params => { return http.post(`${base}/template/recesave`, params).then(res => res.data); };
//模板标签信息
export const getShopmessagetemplate = params => { return http.get(`${base}/template/${params.id}`, params).then(res => res.data); };
//模板保存
export const saveShopmessagetemplate = params => { return http.put(`${base}/template`, params).then(res => res.data); };
//消息详情
export const getMessageDetail = params => { return http.get(`${base}/template/detail/${params.id}`, params).then(res => res.data); };



// 查询规格分组及规格列表
// export const attributegroupListSpec = params => { return http.get(`${base}/attributegroup/list/spec`, params,requestType).then(res => res.data); };
// -----------------------------------------------------------------------------------------------------------------
export const goodsTime = params => { return http.get(`${base}/goods/show/timing/${params.id}`).then(res => res.data); }

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
/*编辑商品活动校验*/
export const goodsUpdateCheckActivity = params => { return http.post(`${base}/goods/check/goods/activity`, params).then(res => res.data); }

// 商品审核--------------------------------------
//修改商品审核状态
export const checkGoods = params => { return http.put(`${base}/goods/check`, params).then(res => res.data); };
//查看商品审核操作流水
export const goodsCheckList = params => { return http.get(`${base}/goods/check/list/${params.goodsId}`, params).then(res => res.data); };

//批量修改商品审核状态
export const checkBatchGoods = params => { return http.put(`${base}/goods/check/batch?goodsStatus=${params.goodsStatus}&rejectReason=${params.rejectReason}`, params.ids).then(res => res.data); };
///结算---------------------------------------------------------------------
//结算详情
export const getbillInfo = params => { return http.get(`${base}/orderbill/${params.id}`, params).then(res => res.data); };
//订单结算列表
export const getOrderBillPage = params => { return http.get(`${base}/orderbill/order/page`, params).then(res => res.data); };
// 退款订单列表
export const getReturnBillPage = params => { return http.get(`${base}/orderbill/return/page`, params).then(res => res.data); };
// 结算日志
export const getBillLog = params => { return http.get(`${base}/orderbill/log?billId=${params.id}`, params).then(res => res.data); };
//结算
export const bill = params => { return http.put(`${base}/orderbill`, params).then(res => res.data); };
//保存对账设置
export const saveBillSetting = params => { return http.put(`${base}/setting/order/bill`, params).then(res => res.data); }
//获取对账设置
export const getBilSetting = params => { return http.get(`${base}/setting/order/bill`, params).then(res => res.data); }
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

//帮助中心
//系统文章
//保存系统文章---------------------------------------------------------------------------------------------------------------------
export const documentAdd = params => { return http.post(`${base}/document`, params).then(res => res.data); };
//修改系统文章---------------------------------------------------------------------------------------------------------------------
export const document = params => { return http.put(`${base}/document`, params).then(res => res.data); };
//删除系统文章---------------------------------------------------------------------------------------------------------------------
export const documentDelete = params => { return http.delete(`${base}/document`, params).then(res => res.data); }
//分页
export const documentPage = params => { return http.get(`${base}/document/page`, params).then(res => res.data); }

//编辑回显数据
export const documentID = params => { return http.get(`${base}/document/${params.id}`, params).then(res => res.data); };


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
//规格下拉列表
export const specAllList = params => { return http.get(`${base}/spec/list`, params, requestType).then(res => res.data); };

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
// 商品规格删除前先看规格有没有关联分类
export const specCheckClass = params => { return http.get(`${base}/specgroup/${params.id}`).then(res => res.data); };

// 标签管理-------------------------------------------------------------------------------------------------------------------------------------
//  列表模块(主要是下拉用)
export const tagList = params => { return http.get(`${base}/label/page`, params, requestType).then(res => res.data); };
//标签下拉列表(查询所有标签分组信息)
export const tagAllList = params => { return http.get(`${base}/labelgroup/list`, params).then(res => res.data); };



//添加标签
export const addTag = params => { return http.post(`${base}/label`, params).then(res => res.data); };
// 编辑回显
export const backScanTag = params => { return http.get(`${base}/label/label/group/${params.id}`).then(res => res.data); }
//编辑标签
export const updateTag = params => { return http.put(`${base}/label`, params).then(res => res.data); };

//标签分组状态修改(启用,禁用)
export const statusTagGroup = params => { return http.put(`${base}/labelgroup/status?groupStatus=${params.groupStatus}&id=${params.id}`, params).then(res => res.data); };

//添加标签分组
export const addTaggroup = params => { return http.post(`${base}/labelgroup`, params).then(res => res.data); };
// 编辑回显
export const backScanTaggroup = params => { return http.get(`${base}/labelgroup/group/label/${params.id}`).then(res => res.data); }
// 编辑标签分组
export const updateTaggroup = params => { return http.put(`${base}/labelgroup`, params).then(res => res.data); };

//标签分组下拉列表(查询所有标签信息)
export const tagGroupAllList = params => { return http.get(`${base}/label/list`, params).then(res => res.data); };


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



// 会员管理--------------------------------------------------------------------------------------------------------------------
// 会员启用与禁用
export const updateMembergrade = params => { return http.put(`${base}/member/state/${params.id}`, params).then(res => res.data); };
//会员编辑详情
export const memberDet = params => { return http.get(`${base}/member/edit/${params.id}`, params).then(res => res.data); };
//会员余额变更
export const memberChangeBalance =  params =>{ return http.put(`${base}/member/change/balance`, params,requestType).then(res => res.data); }; 
//会员余额批量变更
export const memberBatchChangeBalance =  params =>{ return http.put(`${base}/member/batch/change/balance`, params).then(res => res.data); }; 
//会员查看详情
export const memberDetTable = params => { return http.get(`${base}/member/${params.id}`, params).then(res => res.data); };
//会员地址
export const memberAdd = params => { return http.get(`${base}/address/${params.id}`, params).then(res => res.data); };
//设置默认地址
export const setDefault = params => { return http.put(`${base}/address/default/flag?id=${params.id}&defaultFlag=${params.defaultFlag}&memberId=${params.memberId}`, params).then(res => res.data); };
//会员等级详情
export const greadDet = params => { return http.get(`${base}/membergrade/${params.id}`, params).then(res => res.data); };
//获取密码
export const restPassword = params => { return http.put(`${base}/member/reset/passwd?id=${params.id}&mobile=${params.mobile}`, params).then(res => res.data); };
//修改会员等级
export const changeGrade = params => { return http.put(`${base}/membergrade`, params).then(res => res.data); };
//新建、保存会员等级
export const addSavaGrade = params => { return http.post(`${base}/membergrade`, params).then(res => res.data); };
//会员订单
export const memberOrders = params => { return http.get(`${base}/order/${params.id}`, params).then(res => res.data); };
//会员等级
export const memberList = params => { return http.get(`${base}/membergrade/list`, params).then(res => res.data); };
//保存会员信息
export const setMemberPass = params => { return http.put(`${base}/member`, params).then(res => res.data); };
//地区list
export const areaList = params => { return http.get(`${base}/area/first/list`, params).then(res => res.data); };
//二级、三级、四级
export const proList = params => { return http.get(`${base}/area/parent/${params.id}`, params).then(res => res.data); };


/*虚拟订单系列*/
/*电子提货码详情*/
export const fetchCodeDetail = params => { return http.get(`${base}/order/fetchcode/detail/${params.id}`, params).then(res => res.data); }
/*核销记录详情*/
export const verifyCodeDetail = params => { return http.get(`${base}/order/verify/detail/${params.id}`, params).then(res => res.data); }


//交易统计
export const transaction = params => { return http.get(`${base}/statis/order/find/transaction`, params).then(res => res.data); };

export const seven = params => { return http.get(`${base}/statis/order/seven/day/statis`, params).then(res => res.data); };
export const single = params => { return http.get(`${base}/statis/order/single/consumer`, params).then(res => res.data); };
// 订单来源数据展示
export const staticOrderSource = params => { return http.get(`${base}/statis/order/source`, params).then(res => res.data); };


//会员统计
export const proportion = params => { return http.get(`${base}/statis/member/member/grade/proportion`, params).then(res => res.data); };

export const statis = params => { return http.get(`${base}/statis/member/member/grow/statis`, params).then(res => res.data); };
// 会员来源统计展示
export const statisMemberSource = params => { return http.get(`${base}/statis/member/source`, params).then(res => res.data); };

//商品统计
export const goodstatis = params => { return http.get(`${base}/statis/goods/class/goods/statis`, params).then(res => res.data); };
export const salestatis = params => { return http.get(`${base}/statis/goods/goods/sale/statis`, params).then(res => res.data); };

//流量统计
export const pagepv = params => { return http.get(`${base}/statis/page/pv`, params).then(res => res.data); };
// 页面流量统计展示
export const pageSource = params => { return http.get(`${base}/statis/page/source`, params).then(res => res.data); };



//商户管理-----------------------------------------------------------------------------------------------------------------------
//新增商户 //创建店铺
export const addStore = params => { return http.post(`${base}/store`, params).then(res => res.data); };
// 店铺等级列表
export const storeGrade = params => { return http.get(`${base}/store/grade/page`, params).then(res => res.data); }
//新增店铺等级
export const addGrade = params => { return http.post(`${base}/store/grade`, params).then(res => res.data); };
//修改店铺等级
export const updatestoreGrade = params => { return http.put(`${base}/store/grade`, params).then(res => res.data); };
//启用禁用店铺等级
export const showGrade = params => { return http.put(`${base}/store/grade/show`, params).then(res => res.data); };
//回显店铺等级信息
export const storeGradeNews = params => { return http.get(`${base}/store/grade/${params.id}`).then(res => res.data); }
//编辑店铺
export const updatestore = params => { return http.put(`${base}/store`, params).then(res => res.data); };
//启用禁用店铺
export const updateEnable = params => { return http.put(`${base}/store/update/store/status?storeId=${params.storeId}&isEnable=${params.isEnable}`, params).then(res => res.data); };
//回显店铺信息
export const storeNews = params => { return http.get(`${base}/store/${params.id}`).then(res => res.data); }
//店铺经营范围
export const storeScope = params => { return http.get(`${base}/goodsclass/page`, params).then(res => res.data); }
//店铺账号查重
export const storeCnki = params => { return http.get(`${base}/store/verify`, params).then(res => res.data); }
// 店铺管理员重置密码
export const storeResetPassword = params => { return http.put(`${base}/store/user/reset/password?id=${params.id}`, params).then(res => res.data); }
// 查询未关联店铺的用户
export const notStoreUser = params => { return http.get(`${base}/store/user/not/store/user`, params).then(res => res.data); }
// 查询店铺下管理员
export const storeUserPage = params => { return http.get(`${base}/store/user/user/page`, params).then(res => res.data); }
// 回显店铺详情
export const storeInfo = params => { return http.get(`${base}/store/info`,params).then(res => res.data); }
// 修改入驻信息信息
export const storeUpdateStore = params => { return http.put(`${base}/store/update/store`, params).then(res => res.data); };
// 创建店铺
export const addStoreSave = params => { return http.post(`${base}/store/save`,params).then(res => res.data); }
// 校验账户是否已经存在
export const storeVerifyAccount = params => { return http.get(`${base}/store/verify`,params).then(res => res.data); }
// 校验手机号和邮箱是否已经存在
export const storeVerifyMobile = params => { return http.get(`${base}/store/verify/mobile`,params).then(res => res.data); }


//财务管理-----------------------------------------------------------------------------------------------------------------------
//支付方式状态修改
export const paymentStatus = params => { return http.put(`${base}/payment`, params).then(res => res.data); }
//支付方式编辑
export const paymentEdit = params => { return http.get(`${base}/payment/payment/config`, params).then(res => res.data); }
//支付方式保存
export const paymentSave = params => { return http.post(`${base}/payment/update/config`, params).then(res => res.data); }
//交易管理--查看订单
export const paymentOrder= params => { return http.get(`${base}/order/orderSn/${params.orderSn}`, params).then(res => res.data); }
//交易管理--查看订单列表
export const paymentOrderList= params => { return http.get(`${base}/order/list/parentordersn/${params.orderParentSn}`, params).then(res => res.data); }

// 移动端首页菜单管理--------------------------------------------------------------------------------------------------------------------
// 移动菜单回显
export const menuNews = params => { return http.get(`${base}/mobile/indexmenu/${params.id}`).then(res => res.data); }
//新增移动菜单
export const addMobbileMenu = params => { return http.post(`${base}/mobile/indexmenu`, params).then(res => res.data); };
//修改移动菜单
export const updateMobbileMenu = params => { return http.put(`${base}/mobile/indexmenu`, params).then(res => res.data); };
//启用禁用菜单
export const isShow = params => { return http.put(`${base}/mobile/indexmenu/is/show`, params).then(res => res.data); };

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
//修改h5移动楼层
export const updateMobbileFloor = params => { return http.put(`${base}/webfloor`, params).then(res => res.data); };
//修改pc移动楼层
export const updatepcFloor = params => { return http.put(`${base}/pc/webfloor`, params).then(res => res.data); };
//新增h5移动楼层
export const addMobbileFloor = params => { return http.post(`${base}/webfloor`, params).then(res => res.data); };
//新增pc移动楼层
export const addpcFloor = params => { return http.post(`${base}/pc/webfloor`, params).then(res => res.data); };
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
export const floorclassList = params => { return http.get(`${base}/goods/classcustom/custom/tree?showType=${params.showType}`, params).then(res => res.data); }

//楼层，分类广告新增
export const floorClassAdveAdd = params => { return http.post(`${base}/adv/special`, params).then(res => res.data); };
//查询已经关联的pc展示分类
export const pcClassId = params => { return http.get(`${base}/adv/relevance`).then(res => res.data); };
//楼层广告类别下拉
export const floorAdveSlect = params => { return http.get(`${base}/webfloor/list/enabled`,params).then(res => res.data); }
//分类广告类别下拉
// export const classAdveSlect = params => { return http.get(`${base}/goods/classcustom/child/show/${params.id}`).then(res => res.data); }



//网站设置
//系统设置--站点消息设置
export const sysWebMesCon = params => { return http.get(`${base}/setting/site`, params).then(res => res.data); }
//系统设置--站点消息保存
export const sysWebMesSave = params => { return http.post(`${base}/setting/site`, params).then(res => res.data); }
//系统设置--审核设置数据
export const sysWebExamineCon = params => { return http.get(`${base}/setting/audit`, params).then(res => res.data); }
//系统设置--保存审核数据
export const sysWebExamineSave = params => { return http.post(`${base}/setting/audit`, params).then(res => res.data); }
//高级设置--订单设置
export const orderAdvCon = params => { return http.get(`${base}/setting/order`, params).then(res => res.data); }
//高级设置--保存订单设置
export const orderAdvSave = params => { return http.post(`${base}/setting/order`, params).then(res => res.data); }
//高级设置--获取发票设置
export const getInvoice = params => { return http.get(`${base}/setting/invoice`, params).then(res => res.data); }
//高级设置--保存发票设置
export const UpdateInvoice = params => { return http.post(`${base}/setting/invoice`, params).then(res => res.data); }
//高级设置--快递设置
export const expressAdvCon = params => { return http.get(`${base}/setting/express`, params).then(res => res.data); }
//高级设置--快递订单设置
export const expressAdvSave = params => { return http.post(`${base}/setting/express`, params).then(res => res.data); }
//高级设置--结算设置
export const settlAdvCon = params => { return http.get(`${base}/setting/settlement`, params).then(res => res.data); }
//高级设置--结算设置设置
export const settlAdvSave = params => { return http.post(`${base}/setting/settlement`, params).then(res => res.data); }
// 获取网站设置的系统设置的用户默认头像设置
export const getAvatars = params => { return http.get(`${base}/setting/default/avatars`, params).then(res => res.data); }
// 设置网站设置的系统设置的用户默认头像设置
export const setAvatars = params => { return http.post(`${base}/setting/default/avatars`, params).then(res => res.data); }
// 保存秒杀设置
export const saveSeckillSetting = params => { return http.put(`${base}/setting/seckill`, params).then(res => res.data); }
// 获取秒杀设置
export const getSeckillSetting = params => { return http.get(`${base}/setting/seckill`, params).then(res => res.data); }


//申请原因管理
//申请原因列表
export const reasonList = params => { return http.get(`${base}/reasondescription/page`, params).then(res => res.data); }
//申请原因编辑
export const addReason = params => { return http.post(`${base}/reasondescription`, params).then(res => res.data); }
//申请原因新增
export const editReason = params => { return http.put(`${base}/reasondescription`, params).then(res => res.data); }


//版本日志管理
//版本日志列表
export const updatelogpage = params => { return http.get(`${base}/updatelog/page`, params).then(res => res.data); }
//版本日志新增
export const addupdatelog = params => { return http.post(`${base}/updatelog`, params).then(res => res.data); }
//版本日志编辑
export const editupdatelog = params => { return http.put(`${base}/updatelog`, params).then(res => res.data); }


//订单管理

//评价详情
export const evaDets = params => { return http.get(`${base}/evaluate/${params.id}`, params).then(res => res.data); }
//商品评价--回复
export const msgReply = params => { return http.put(`${base}/evaluate/reply`, params).then(res => res.data); }
//商品评价--显示/隐藏
export const changeStatus = params => { return http.put(`${base}/evaluate/batch`, params).then(res => res.data); }

//商品评价 --批量修改读取状态
export const changeReadStatus = params => { return http.put(`${base}/evaluate/batch/read/state`, params).then(res => res.data); }

//退货详情
export const returnDetail = params => { return http.get(`${base}/aftersale/return/detail/${params.aftersaleSn}`, params).then(res => res.data); }
//支付方式
export const paymentList = params => { return http.get(`${base}/payment/list`, params).then(res => res.data); }




// 图片--------------------------------------------------------------------------------------------------------------------
//获取图片分组列表
export const getpicturecategoryallapi = params => { return http.get(`${base}/picture/category/all`, params).then(res => res.data); }
//新增一个分组列表
export const addpicturecategory = params => { return http.post(`${base}/picture/category`, params).then(res => res.data); }
//图片分组的删除
export const deleteImage = params => { return http.delete(`${base}/picture/category`, params).then(res => res.data); }
//修改图片分组名称
export const updataPictureCategory = params => { return http.put(`${base}/picture/category`, params).then(res => res.data); }
//删除图片
export const deletepicture = params => { return http.delete(`${base}/picture`, params).then(res => res.data); }
//批量保存图片  目前不用
export const saveimagesurl = params => { return http.post(`${base}/picture`, params).then(res => res.data); }


//订单详情 ---根据订单id查询
export const orderDetail = params => { return http.get(`${base}/order/${params.id}`, params).then(res => res.data); }
//订单详情 ---根据订单编号查询
export const orderDetailBybh = params => { return http.get(`${base}/order/${params.aftersaleSn}`, params).then(res => res.data); }

//订单明细
export const toOrderSnDetail = params => { return http.get(`${base}/order/activity/detail/orderSn/${params.orderSn}`, params).then(res => res.data); }



//换货详情
export const exchDetail = params => { return http.get(`${base}/aftersale/barter/detail/${params.aftersaleSn}`, params).then(res => res.data); }
// 售后审核详情
export const salesAuditDetail = params => { return http.get(`${base}/aftersale/audit/detail/${params.aftersaleSn}`, params).then(res => res.data); }
//售后详情-售后申请审核
export const salesDetail = params => { return http.get(`${base}/aftersale/apply/detail/${params.aftersaleSn}`, params).then(res => res.data); }
//售后申请审核
export const salesExamine = params => { return http.get(`${base}/aftersale/apply/${params.id}`, params).then(res => res.data); }
//退换货设置列表
export const goodSetting = params => { return http.get(`${base}/setting/aftersale`).then(res => res.data); }
//更新退换货设置
export const updataGoodSetting = params => { return http.post(`${base}/setting/aftersale`, params).then(res => res.data); }
//审核退换货
export const examineGoods = params => { return http.post(`${base}/aftersale/apply/reason`, params).then(res => res.data); }
//仲裁管理
export const arbitrationPage = params => { return http.get(`${base}/aftersale/arbitration/page`, params).then(res => res.data); }

//仲裁详情
export const arbitrationDetail = params => { return http.get(`${base}/arbitration/detail/${params.id}`, params).then(res => res.data); }
//仲裁审核
export const arbitrationAudit = params => { return http.put(`${base}/arbitration/audit`, params).then(res => res.data); }

// 管理员重置密码
export const resetPassword = params => { return http.put(`${base}/user/reset/password?id=${params.id}`, params).then(res => res.data); }

// 网站-搜索管理-商品索引同步
export const goodsIndexInit = params => { return http.get(`${base}/search/goods`, params).then(res => res.data); }
//// 网站-搜索管理-规格索引同步
export const specIndexInit = params => { return http.get(`${base}/search/spec`, params).then(res => res.data); }
// 网站-搜索管理-优惠券索引同步
export const couponsIndexInit = params => { return http.get(`${base}/search/coupons`, params).then(res => res.data); }
// 网站-搜索管理-满减活动索引同步
export const reduceIndexInit = params => { return http.get(`${base}/search/reduce`, params).then(res => res.data); }
// 网站-搜索管理-秒杀活动索引同步
export const seckillIndexInit = params => { return http.get(`${base}/search/seckill`, params).then(res => res.data); }
// 网站-搜索管理-拼团活动索引同步
export const groupIndexInit = params => { return http.get(`${base}/search/group`, params).then(res => res.data); }
// 网站-搜索管理-店铺索引同步
export const storeIndexInit = params => { return http.get(`${base}/search/store`, params).then(res => res.data); }
// 网站-搜索管理-店铺索引同步
export const flashSaleIndexInit = params => { return http.get(`${base}/search/flash/sale`, params).then(res => res.data); }

// 运费模板
/*店铺运费模板列表*/
export const storeFreightTemplateList = params => { return http.get(`${base}/freight/template/list?storeId=${params.storeId}`, params).then(res => res.data); }


// 退款服务管理
export const wxRefund = params => { return http.put(`${base}/refund/mangage/wx/refund?id=${params.id}`, params).then(res => res.data); }

//同义词管理接口--------------------------------------------------------------------------------------------------------------------
// 删除
export const deleteShopsynonym = params => { return http.delete(`${base}/synonym`, params).then(res => res.data); };
// 保存
export const shopsynonymSave = params => { return http.post(`${base}/synonym`, params).then(res => res.data); };
// 修改
export const editShopsynonym = params => { return http.put(`${base}/synonym`, params).then(res => res.data); };
//批量修改
export const editShopsynonymBatch = params => { return http.put(`${base}/synonym/update/batch`, params).then(res => res.data); };
// 分页
export const shopsynonymPage = params => { return http.get(`${base}/synonym/page`, params).then(res => res.data); };
// 信息
export const backScanShopsynonym = params => { return http.get(`${base}/synonym/${params.id}`, params).then(res => res.data); };

// 修改
export const editShophotkeyword = params => { return http.put(`${base}/hotword`, params).then(res => res.data); };
// 保存
export const shophotkeywordSave = params => { return http.post(`${base}/hotword`, params).then(res => res.data); };
// 信息
export const backScanShophotkeyword = params => { return http.get(`${base}/hotword/${params.id}`, params).then(res => res.data); };

// 积分设置-------------------------------------------------------------------------------------------------------------
export const updatehoppointssettings = params => { return http.put(`${base}/point/setting/reward`, params).then(res => res.data); };

export const getShoppointssettings = params => { return http.get(`${base}/point/setting/reward`, params).then(res => res.data); };

// 积分设置 更多规则设置
export const updateMoresettings = params => { return http.put(`${base}/point/setting/more/rule`, params).then(res => res.data); };

export const getMoresettings = params => { return http.get(`${base}/point/setting/more/rule`, params).then(res => res.data); };
//店铺审核--------------------------------------------------------------------------------------------------------------------
// 店铺入住详情
export const storeAuditInfo = params => { return http.get(`${base}/audit/info`, params).then(res => res.data); };
// 审核
export const shopAuditUpdateStatus = params => { return http.put(`${base}/audit/update/status`, params).then(res => res.data); };
// 操作流水
export const storeAuditTally= params => { return http.get(`${base}/audit/tally`, params).then(res => res.data); };


// 店铺普通信息审核列表
// export const storeAuditbBsicPage= params => { return http.get(`${base}/audit/basic/page`, params).then(res => res.data); };
// 店铺普通信息审核详情
export const storeAuditbaseInfo = params => { return http.get(`${base}/audit/basic/info`, params).then(res => res.data); };

// 店铺公司信息审核列表
// export const storeAuditCompanyPage = params => { return http.get(`${base}/audit/company/page`, params).then(res => res.data); };
// 店铺公司信息审核详情
export const storeAuditCompanyInfo= params => { return http.get(`${base}/audit/company/info`, params).then(res => res.data); };

// 导出管理
export const sysexportmanagementPage = params => { return http.get(`${base}/sysexportmanagement/page`, params).then(res => res.data); };
// 启用禁用用户
export const isEnable = params => { return http.put(`${base}/store/user/isenable?userId=${params.id}&type=${params.type}`,params).then(res => res.data); }
//首页--------------------------------------------------------------------------------------------------------------------
//管理端基础概况
export const indexBaseManager= params => { return http.get(`${base}/home/base`, params).then(res => res.data); };
//商户销售排名
export const storeSellRaking= params => { return http.get(`${base}/home/store/sell`, params).then(res => res.data); };
//商品销量排名
export const goodsSellRaking= params => { return http.get(`${base}/home/goods/sell`, params).then(res => res.data); };
// 获取用户配置的首页功能按钮
export const userFunctionList= params => { return http.get(`${base}/home/user/function`, params).then(res => res.data); };

// 保存用户配置的首页功能按钮
export const saveUserFn= params => { return http.post(`${base}/userfunction/batch`, params).then(res => res.data); };

// // 获取用户角色拥有的首页功能按钮
// export const userFunctionAllList= params => { return http.get(`${base}/userfunction/all/menu`, params).then(res => res.data); };
// 投诉----------------------------------------------------------------------
export const complainType= params => { return http.get(`${base}/ordercomplain/type`, params).then(res => res.data); };
// 反馈
export const feedbackType= params => { return http.get(`${base}/feedback/type`, params).then(res => res.data); };
// 地区详情
export const info = params => { return http.get(`${base}/area/info?id=${params.id}`, params).then(res => res.data); };
// 编辑地区
export const updateArea = params => { return http.put(`${base}/area/update`, params).then(res => res.data); };
// 地址集合
export const treeList = params => { return http.get(`${base}/area/tree`, params).then(res => res.data); };
// 新增地区
export const saveArea = params => { return http.post(`${base}/area/save`, params).then(res => res.data); };
// 查询父ID信息
export const parentInfo = params => { return http.get(`${base}/area/parent?parentId=${params.id}`, params).then(res => res.data); };
// 查询所有大区
export const regionList = params => { return http.get(`${base}/area/region`, params).then(res => res.data); };

//禁用词分页
export const getadvertisingban = params =>  { return http.get(`${base}/stopword/page`, params).then(res => res.data); };

//添加禁用词
export const addadvertisingban = params => { return http.post(`${base}/stopword/add`, params).then(res => res.data); };
//编辑禁用词
export const updateadvertisingban = params => { return http.put(`${base}/stopword/update`, params).then(res => res.data); };

//提现申请审核通过或者驳回
export const withdrawalapplicationAudit = params => { return http.put(`${base}/withdrawalapplication/audit`, params).then(res => res.data); };

//提现发放审核通过或者驳回
export const withdrawalapplicationIssue = params => { return http.put(`${base}/withdrawalapplication/issue`, params).then(res => res.data); };



