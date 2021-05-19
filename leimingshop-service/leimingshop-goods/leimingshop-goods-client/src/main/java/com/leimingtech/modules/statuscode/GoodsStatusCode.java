/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.statuscode;

import com.leimingtech.exception.ServiceStatusCode;

/**
 * @Description
 * @Data: 2019/7/9 10:23
 * @Author: chengqian
 * @Version: 1.0
 */
public class GoodsStatusCode extends ServiceStatusCode {

    /*========================================================== 评价 ==================================================================*/

    /**
     * 分页查询商品评价
     */
    public static final String ADMIN_EVALUATE_PAGE_SUCCESS = "200201";
    public static final String ADMIN_EVALUATE_PAGE_SUCCESS_MESSAGE = "分页查询评价";
    /**
     * 保存商品评价
     */
    public static final String ADMIN_EVALUATE_SAVE_SUCCESS = "200202";
    public static final String ADMIN_EVALUATE_SAVE_SUCCESS_MESSAGE = "保存商品评价";
    /**
     * 回复评价 admin
     */
    public static final String ADMIN_EVALUATE_REPLY_SUCCESS = "200203";
    public static final String ADMIN_EVALUATE_REPLY_SUCCESS_MESSAGE = "admin回复商品评价";

    /**
     * 回复评价 seller
     */
    public static final String SELLER_EVALUATE_REPLY_SUCCESS = "200204";
    public static final String SELLER_EVALUATE_REPLY_SUCCESS_MESSAGE = "seller回复商品评价";
    /**
     * 修改评论次数
     */
    public static final String UPDATE_EVALUATE_NUM_FAIL = "200227";
    public static final String UPDATE_EVALUATE_NUM_FAIL_MESSAGE = "修改评论次数失败，商品不存在";

    /*========================================================== 价格记录 ==================================================================*/


    /**
     * 保存价格修改记录
     */
    public static final String PRICE_LOG_SAVE_SUCCESS = "200205";
    public static final String PRICE_LOG_SAVE_SUCCESS_MESSAGE = "保存价格修改记录";
    /**
     * 库存修改记录
     */
    public static final String STOCK_LOG_SAVE_SUCCESS = "200206";
    public static final String STOCK_LOG_SAVE_SUCCESS_MESSAGE = "保存库存修改记录";


    /*========================================================== 商品分类 ==================================================================*/

    /**
     * 商品分类保存
     */
    public static final String GOODS_CLASS_SAVE_OPERATION_CODE = "200207";
    public static final String GOODS_CLASS_SAVE_OPERATION_MESSAGE = "商品分类保存";
    /**
     * 商品分类修改
     */
    public static final String GOODS_CLASS_UPDATE_OPERATION_CODE = "200208";
    public static final String GOODS_CLASS_UPDATE_OPERATION_MESSAGE = "商品分类修改";
    /**
     * 商品分类删除
     */
    public static final String GOODS_CLASS_DELETE_OPERATION_CODE = "200209";
    public static final String GOODS_CLASS_DELETE_OPERATION_MESSAGE = "商品分类删除";
    /**
     * 商品分类保存
     */
    public static final String GOODS_CLASS_EXPORT_OPERATION_CODE = "200210";
    public static final String GOODS_CLASS_EXPORT_OPERATION_MESSAGE = "商品分类导出";
    /**
     * 商品分类保存
     */
    public static final String GOODS_CLASS_IMPORT_OPERATION_CODE = "200211";
    public static final String GOODS_CLASS_IMPORT_OPERATION_MESSAGE = "商品分类导入";


    /*========================================================== 商品 ==================================================================*/

    /**
     * 成功: 200  失败: 500  参数错误: 400   01:商品 02:规格 03:属性 ..
     *
     */

    /**
     * 商品列表查询成功
     */
    public static final String GOODS_PAGE_SUCCESS_CODE = "200212";
    public static final String GOODS_PAGE_SUCCESS_MSG = "商品列表分页查询成功";

    /**
     * 商品审核列表查询成功
     */
    public static final String GOODS_VERIFY_PAGE_SUCCESS_CODE = "200213";
    public static final String GOODS_VERIFY_PAGE_SUCCESS_MSG = "商品审核列表分页查询成功";

    /**
     * 商品详情查询成功
     */
    public static final String GOODS_DETAIL_SUCCESS_CODE = "200214";
    public static final String GOODS_DETAIL_SUCCESS_MSG = "商品详情查询成功";

    /**
     * 商品保存
     */
    public static final String GOODS_SAVE_SUCCESS_CODE = "200215";
    public static final String GOODS_SAVE_SUCCESS_MSG = "商品信息保存成功";
    public static final String GOODS_SAVE_SUCCESS_NEWID_MSG = "商品保存--规格替换属性值id";
    public static final String GOODS_SAVE_SUCCESS_GOODS_MSG = "商品发布完成，结果成功，商品ID:";

    /**
     * 商品修改
     */
    public static final String GOODS_UPDATE_SUCCESS_CODE = "200216";
    public static final String GOODS_UPDATE_SUCCESS_MSG = "商品信息修改成功。";
    public static final String GOODS_UPDATE_SUCCESS_DEL_VAL_MSG = "商品修改--移除规格属性值信息: ";
    public static final String GOODS_UPDATE_SUCCESS_DEL_REL_MSG = "商品修改--移除中间表数据: ";
    public static final String GOODS_UPDATE_SUCCESS_DEL_SPEC_MSG = "商品修改--删除规格id数组: ";
    public static final String GOODS_UPDATE_SUCCESS_NEW_VAL_MSG = "商品修改--新增属性值保存: ";
    public static final String GOODS_UPDATE_SUCCESS_UPDATE_GOODS_MSG = "商品编辑完成，结果成功，商品ID: ";

    /**
     * 修改上下架状态
     */
    public static final String GOODS_SHOW_UPDATE_CODE = "200217";
    public static final String GOODS_SHOW_UPDATE_MSG = "修改商品上下架状态";
    public static final String GOODS_SHOW_UPDATE_SUCCESS_MSG = "修改商品上下架状态成功";
    /**
     * 修改上下架状态
     */
    public static final String TIMED_GOODS_SHOW_UPDATE_CODE = "200218";
    public static final String TIMED_GOODS_SHOW_UPDATE_MSG = "保存定时上下架商品信息";
    public static final String TIMED_GOODS_SHOW_UPDATE_SUCCESS_MSG = "保存定时上下架商品信息成功";

    /**
     * 修改上下架状态失败
     */
    public static final String GOODS_SHOW_UPDATE_ERROR_CODE = "500218";
    public static final String GOODS_SHOW_UPDATE_ERROR_MSG = "商品上下架状态修改失败";


    /**
     * 修改审核状态
     */
    public static final String GOODS_STATUS_UPDATE_CODE = "200219";
    public static final String GOODS_STATUS_UPDATE_MSG = "执行商品审核操作";
    /**
     * 商品审核列表导出
     */
    public static final String GOODS_STATUS_EXPORT_CODE = "200220";
    public static final String GOODS_STATUS_EXPORT_MSG = "商品审核列表导出";
    /**
     * 商品列表导出
     */
    public static final String GOODS_LIST_EXPORT_CODE = "200221";
    public static final String GOODS_LIST_EXPORT_MSG = "商品列表导出";

    /**
     * 商品删除成功
     */
    public static final String GOODS_DELETE_SUCCESS_CODE = "200222";
    public static final String GOODS_DELETE_SUCCESS_MSG = "商品删除成功";

    /*========================================================== 商品规格操作日志 ==================================================================*/
    /**
     * 修改SKU
     */
    public static final String SKU_UPDATE_OPERATION_CODE = "200223";
    public static final String SKU_UPDATE_OPERATION_MESSAGE = "执行修改SKU操作";
    public static final String SKU_UPDATE_OPERATION_SUCCESS_MESSAGE = "SKU修改成功";

    /**
     * 规格上下架操作
     */
    public static final String SPEC_SHOW_UPDATE_OPERATION_CODE = "200224";
    public static final String SPEC_SHOW_UPDATE_OPERATION_MESSAGE = "执行规格上下架操作";
    public static final String SPEC_SHOW_UPDATE_OPERATION_SUCCESS_MESSAGE = "规格上下架修改成功";
    public static final String SPEC_SHOW_UPDATE_OPERATION_SUCCESS_BY_GOODSID_MESSAGE = "根据商品ID修改规格上下架状态成功";


    /**
     * 商品价格批量修改
     */
    public static final String GOODS_SPEC_PRICE_UPDATE_OPERATION_CODE = "200225";
    public static final String GOODS_SPEC_PRICE_UPDATE_OPERATION_MESSAGE = "执行商品规格价格批量修改操作";
    public static final String GOODS_SPEC_PRICE_UPDATE_OPERATION_SUCCESS_MESSAGE = "商品规格价格修改成功";

    /**
     * 商品库存批量修改
     */
    public static final String GOODS_SPEC_STORAGE_UPDATE_OPERATION_CODE = "200226";
    public static final String GOODS_SPEC_STORAGE_UPDATE_OPERATION_MESSAGE = "执行商品规格库存批量修改操作";
    public static final String GOODS_SPEC_STORAGE_UPDATE_OPERATION_SUCCESS_MESSAGE = "商品规格库存修改成功";


    /**
     * 商品发布数量超出
     */
    public static final ServiceStatusCode GOODS_PUBLISH_COUNT_OUT = new GoodsStatusCode("400218", "已发布的商品已达到当前店铺等级的最大值");

    public static final ServiceStatusCode GOODS_STATUS_ERROR = new GoodsStatusCode("400210", "商品未通过审核不能进行操作");

    public static final ServiceStatusCode GOODS_SHOW_STATUS_ERROR = new GoodsStatusCode("400211", "操作状态与商品当前状态一致，操作失败");

    public static final ServiceStatusCode GOODS_ACTIVITY_START_ERROR = new GoodsStatusCode("400212", "该商品参加的活动已开始，无法编辑商品");
    public static final ServiceStatusCode GOODS_CLASS_IMPORT_ERROR = new GoodsStatusCode("400213", "导入商品分类的类型错误");


    /**
     * 商品分类已删除
     */
    public static final ServiceStatusCode GOODS_CLASS_IS_DEL = new GoodsStatusCode("400100", "选择的商品分类已删除");

    protected GoodsStatusCode(String code, String message, Object... arguments) {
        super(code, message, arguments);
    }

    @Override
    public ServiceStatusCode copy(String var1, Object... var2) {
        return null;
    }
}
