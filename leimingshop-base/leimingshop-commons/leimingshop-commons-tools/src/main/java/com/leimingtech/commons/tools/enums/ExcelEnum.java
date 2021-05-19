/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.commons.tools.enums;

import lombok.Getter;

/**
 * 功能描述：
 * excel导出枚举
 *
 * @author 宋文豪
 * @email: songwenhao@leimingtech.com
 * @Date : 2020/3/10
 **/

@Getter
public enum ExcelEnum {

    /**
     * 商品导出
     */
    GOODS_EXPORT("goods_export", "商品导出"),
    /**
     * 商品审核导出
     */
    GOODS_CHECK_EXPORT("goods_check_export", "商品审核导出"),
    /**
     * 商品导出到模板
     */
    GOODS_EXPORT_TEMPLATE("goods_export_template", "商品导出到模板"),
    /**
     * 操作日志导出
     */
    OPERATION_LOG_EXPORT("opertion_log_export", "操作日志导出"),
    /**
     * 订单导出
     */
    ORDER_EXPORT("order_export", "订单导出"),
    /**
     * 会员信息导出
     */
    MEMBER_EXPORT("member_export", "会员导出");
    private String code;

    private String name;

    ExcelEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }


}
