/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.excel.coupons;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 会员优惠券实体
 *
 * @author 刘远杰 2634443725@qq.com
 * @since v1.0.0 2019-12-05
 */
@Data
public class MemberCouponsExcel {
    @ExcelProperty(value = "ID")
    private Long id;

    @ExcelProperty(value = "优惠券活动id")
    private Long activityId;

    @ExcelProperty(value = "会员id")
    private Long memberId;

    @ExcelProperty(value = "会员名称")
    private String memberName;

    @ExcelProperty(value = "优惠券状态 未使用(0不可使用 1可使用) 2已使用 3已过期")
    private Integer couponsState;

    @ExcelProperty(value = "使用开始时间")
    private Date startDate;

    @ExcelProperty(value = "使用结束时间")
    private Date endDate;

    @ExcelProperty(value = "使用时间")
    private Date useDate;

    @ExcelProperty(value = "订单编号")
    private Long orderSn;

    @ExcelProperty(value = "订单id")
    private Long orderId;

    @ExcelProperty(value = "商品总价格")
    private BigDecimal goodsAmount;

    @ExcelProperty(value = "支付总金额")
    private BigDecimal orderAmount;

    @ExcelProperty(value = "创建人")
    private String creator;

    @ExcelProperty(value = "创建时间")
    private Date createDate;

    @ExcelProperty(value = "更新人")
    private String updater;

    @ExcelProperty(value = "更新时间")
    private Date updateDate;

}
