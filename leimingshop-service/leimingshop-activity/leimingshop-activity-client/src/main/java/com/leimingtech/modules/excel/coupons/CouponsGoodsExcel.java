/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.excel.coupons;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 优惠券活动商品实体
 *
 * @author 刘远杰 2634443725@qq.com
 * @since v1.0.0 2019-12-05
 */
@Data
public class CouponsGoodsExcel {
    @ExcelProperty(value = "ID")
    private Long id;

    @ExcelProperty(value = "优惠券活动id")
    private Long activityId;

    @ExcelProperty(value = "活动商品范围 0全场通用 1指定店铺分类 2指定商品 3指定品牌")
    private Integer activityGoodsScope;

    @ExcelProperty(value = "关联id")
    private Long relationId;

    @ExcelProperty(value = "关联名称")
    private String relationName;

    @ExcelProperty(value = "创建人")
    private String creator;

    @ExcelProperty(value = "创建时间")
    private Date createDate;

    @ExcelProperty(value = "更新人")
    private String updater;

    @ExcelProperty(value = "更新时间")
    private Date updateDate;

}
