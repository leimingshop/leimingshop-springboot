/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.excel.coupons;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 优惠券活动实体
 *
 * @author 刘远杰 2634443725@qq.com
 * @since v1.0.0 2019-12-05
 */
@Data
public class CouponsActivityExcel {
    @ExcelProperty(value = "ID")
    private Long id;

    @ExcelProperty(value = "优惠券活动名称")
    private String activityName;

    @ExcelProperty(value = "活动范围 0平台 1店铺")
    private Integer activityScope;

    @ExcelProperty(value = "店铺id")
    private Long storeId;

    @ExcelProperty(value = "店铺名称")
    private String storeName;

    @ExcelProperty(value = "优惠券类型 0满减券 1满折券")
    private Integer couponsType;

    @ExcelProperty(value = "活动场景 0普通券 1新人券")
    private Integer activityScene;

    @ExcelProperty(value = "优惠券总数")
    private Long totalNum;

    @ExcelProperty(value = "每人限领数量")
    private Long personLimit;

    @ExcelProperty(value = "会员等级id")
    private Long memberGraderId;

    @ExcelProperty(value = "会员等级名称")
    private String memberGraderName;

    @ExcelProperty(value = "使用限制金额")
    private BigDecimal limitAmount;

    @ExcelProperty(value = "优惠券面额")
    private BigDecimal faceValue;

    @ExcelProperty(value = "领取开始时间")
    private Date getStartDate;

    @ExcelProperty(value = "领取结束时间")
    private Date getEndDate;

    @ExcelProperty(value = "有效期类型 0固定时间 1有效天数")
    private Integer validityType;

    @ExcelProperty(value = "使用开始时间")
    private Date useStartDate;

    @ExcelProperty(value = "使用结束时间")
    private Date useEndDate;

    @ExcelProperty(value = "有效天数")
    private Integer validityDays;

    @ExcelProperty(value = "优惠券剩余数量")
    private Long surplusNum;

    @ExcelProperty(value = "活动商品范围 0全场通用 1指定店铺分类 2指定商品 3指定品牌")
    private Integer activityGoodsScope;

    @ExcelProperty(value = "是否与其他活动共享 0不共享 1共享")
    private Integer shareFlag;

    @ExcelProperty(value = "审核状态 0未审核 1审核通过 2审核未通过")
    private Integer auditState;

    @ExcelProperty(value = "活动状态 0未开始 1进行中 2已结束")
    private Integer activityState;

    @ExcelProperty(value = "活动描述")
    private String activityDescription;

    @ExcelProperty(value = "创建人")
    private String creator;

    @ExcelProperty(value = "创建时间")
    private Date createDate;

    @ExcelProperty(value = "更新人")
    private String updater;

    @ExcelProperty(value = "更新时间")
    private Date updateDate;

}
