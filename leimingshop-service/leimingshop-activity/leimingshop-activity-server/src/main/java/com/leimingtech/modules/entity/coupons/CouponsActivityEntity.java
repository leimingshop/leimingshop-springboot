/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.coupons;

import com.baomidou.mybatisplus.annotation.TableName;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 优惠券活动实体
 *
 * @author 刘远杰 2634443725@qq.com
 * @since v1.0.0 2019-12-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_coupons_activity")
public class CouponsActivityEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 优惠券活动名称
     */
    private String activityName;

    /**
     * 活动范围 0平台 1店铺
     */
    private Integer activityScope;

    /**
     * 店铺id
     */
    private Long storeId;

    /**
     * 店铺名称
     */
    private String storeName;

    /**
     * 店铺logo
     */
    private String storeLogo;

    /**
     * 优惠券类型 0满减券 1满折券
     */
    private Integer couponsType;

    /**
     * 活动场景 0普通券 1新人券
     */
    private Integer activityScene;

    /**
     * 优惠券总数
     */
    private Long totalNum;

    /**
     * 每人限领数量
     */
    private Long personLimit;

    /**
     * 会员等级id
     */
    private Long memberGraderId;

    /**
     * 会员等级名称
     */
    private String memberGraderName;

    /**
     * 使用限制金额
     */
    private BigDecimal limitAmount;

    /**
     * 优惠券面额
     */
    private BigDecimal faceValue;

    /**
     * 领取开始时间
     */
    private Date getStartDate;

    /**
     * 领取结束时间
     */
    private Date getEndDate;

    /**
     * 有效期类型 0固定时间 1有效天数
     */
    private Integer validityType;

    /**
     * 使用开始时间
     */
    private Date useStartDate;

    /**
     * 使用结束时间
     */
    private Date useEndDate;

    /**
     * 有效天数
     */
    private Integer validityDays;

    /**
     * 优惠券剩余数量
     */
    private Long surplusNum;

    /**
     * 活动商品范围 0全场通用 1指定店铺分类 2指定商品 3指定品牌
     */
    private Integer activityGoodsScope;

    /**
     * 是否与其他活动共享 0不共享 1共享
     */
    private Integer shareFlag;

    /**
     * 审核状态 0未审核 1审核通过 2审核未通过
     */
    private Integer auditState;

    /**
     * 活动状态 0未开始 1进行中 2已结束
     */
    private Integer activityState;

    /**
     * 活动描述
     */
    private String activityDescription;

}
