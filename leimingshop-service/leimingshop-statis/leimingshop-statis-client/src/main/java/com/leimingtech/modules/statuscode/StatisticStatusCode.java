/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.statuscode;

import com.leimingtech.exception.ServiceStatusCode;

/**
 * @author xuzhch
 * @className StatisticStatusCode
 * @description 统计模块日志状态码
 * @date 2020/1/9/009
 */
public class StatisticStatusCode extends ServiceStatusCode {
    /*========================================================== 订单统计 ==================================================================*/
    /**
     * 订单统计列表
     */
    public static final String TRANSACTION_STATISTIC_CODE = "200101";
    public static final String TRANSACTION_STATISTIC_MASSAGE = "进入交易统计查询";

    /**
     * 七天交易统计列表
     */
    public static final String SEVEN_DAY_TRANSACTION_STATISTIC_CODE = "200102";
    public static final String SEVEN_DAY_TRANSACTION_STATISTIC_MASSAGE = "进入近七天统计查询";

    /**
     * 单人消费统计列表
     */
    public static final String SINGLE_CONSUME_STATISTIC_CODE = "200103";
    public static final String SINGLE_CONSUME_STATISTIC_MASSAGE = "进入单人消费统计查询";
    /**
     * 交易统计数据保存
     */
    public static final String TRANSACTION_STATISTIC_DATA_SAVE_CODE = "200104";
    public static final String TRANSACTION_STATISTIC_DATA_SAVE_MASSAGE = "交易统计数据保存成功";
    /**
     * 交易统计未保存
     */
    public static final String TRANSACTION_STATISTIC_DATA_REPEAT_SAVE_CODE = "200105";
    public static final String TRANSACTION_STATISTIC_DATA_REPEAT_SAVE_MASSAGE = "交易统计数据重复(未执行保存)";
    /**
     * 订单统计数据保存
     */
    public static final String ORDER_STATISTIC_DATA_SAVE_CODE = "200106";
    public static final String ORDER_STATISTIC_DATA_SAVE_MASSAGE = "订单统计数据保存成功";
    /**
     * 订单统计未保存
     */
    public static final String ORDER_STATISTIC_DATA_REPEAT_SAVE_CODE = "200107";
    public static final String ORDER_STATISTIC_DATA_REPEAT_SAVE_MASSAGE = "订单统计数据重复(未执行保存)";
    /**
     * 订单来源数据展示
     */
    public static final String ORDER_SOURCE_STATISTI_CODE = "200108";
    public static final String ORDER_SOURCE_STATISTIC_MASSAGE = "订单来源数据列表";

    /*========================================================== 商品统计 ==================================================================*/
    /**
     * 商品分类销量
     */
    public static final String GOODS_CLASS_SALES_STATISTIC_CODE = "200201";
    public static final String GOODS_CLASS_SALES_STATISTIC_MASSAGE = "进入商品分类销量统计查询";
    /**
     * 商品分类销量
     */
    public static final String SINGLE_GOODS_SALES_STATISTIC_CODE = "200202";
    public static final String SINGLE_GOODS_SALES_STATISTIC_MASSAGE = "进入商品销量统计查询";
    /**
     * 商品统计数据保存
     */
    public static final String GOODS_STATISTIC_DATA_SAVE_CODE = "200203";
    public static final String GOODS_STATISTIC_DATA_SAVE_MASSAGE = "商品统计数据保存成功";
    /**
     * 商品统计未保存
     */
    public static final String GOODS_STATISTIC_DATA_REPEAT_SAVE_CODE = "200204";
    public static final String GOODS_STATISTIC_DATA_REPEAT_SAVE_MASSAGE = "商品统计数据重复(未执行保存)";



    /*========================================================== 流量统计 ==================================================================*/
    /**
     * 页面流量统计
     */
    public static final String PAGE_VIEW_STATISTIC_CODE = "200301";
    public static final String PAGE_VIEW_STATISTIC_MASSAGE = "进入页面流量统计查询";
    /*========================================================== 会员统计 ==================================================================*/
    /**
     * 会员增长统计
     */
    public static final String MEMBER_GROW_STATISTIC_CODE = "200401";
    public static final String MEMBER_GROW_STATISTIC_MASSAGE = "进入会员增长统计查询";
    /**
     * 会员等级占比统计
     */
    public static final String MEMBER_GRADE_RATIO_STATISTIC_CODE = "200402";
    public static final String MEMBER_GRADE_RATIO_STATISTIC_MASSAGE = "进入会员等级占比统计查询";

    /**
     * 会员增长统计保存
     */
    public static final String MEMBER_STATISTIC_DATA_SAVE_CODE = "200403";
    public static final String MEMBER_STATISTIC_DATA_SAVE_MASSAGE = "会员统计数据保存成功";
    public static final String MEMBER_STATISTIC_DATA_NOT_SAVE_MASSAGE = "会员统计无数据，未保存成功";
    /**
     * 会员增长统计未保存
     */
    public static final String MEMBER_STATISTIC_DATA_REPEAT_SAVE_CODE = "200404";
    public static final String MEMBER_STATISTIC_DATA_REPEAT_SAVE_MASSAGE = "会员统计数据重复(未保存)";

    /**
     * 会员等级占比统计保存
     */
    public static final String MEMBER_GRADE_RATIO_STATISTIC_DATA_SAVE_CODE = "200405";
    public static final String MEMBER_GRADE_RATIO_STATISTIC_DATA_SAVE_MASSAGE = "会员统计数据保存成功";
    /**
     * 会员等级占比统计未保存
     */
    public static final String MEMBER_GRADE_RATIO_STATISTIC_DATA_REPEAT_SAVE_CODE = "200406";
    public static final String MEMBER_GRADE_RATIO_STATISTIC_DATA_REPEAT_SAVE_MASSAGE = "会员统计数据重复(未保存)";

    protected StatisticStatusCode(String code, String message, Object... arguments) {
        super(code, message, arguments);
    }

    @Override
    public ServiceStatusCode copy(String var1, Object... var2) {
        return null;
    }
}
