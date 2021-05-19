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
 * 会员优惠券实体
 *
 * @author 刘远杰 2634443725@qq.com
 * @since v1.0.0 2019-12-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_member_coupons")
public class MemberCouponsEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 优惠券活动id
     */
    private Long activityId;

    /**
     * 会员id
     */
    private Long memberId;

    /**
     * 会员名称
     */
    private String memberName;

    /**
     * 优惠券状态 未使用(0不可使用 1可使用) 2已使用 3已过期
     */
    private Integer couponsState;

    /**
     * 使用开始时间
     */
    private Date startDate;

    /**
     * 使用结束时间
     */
    private Date endDate;

    /**
     * 使用时间
     */
    private Date useDate;

    /**
     * 订单编号
     */
    private Long orderSn;

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 商品总价格
     */
    private BigDecimal goodsAmount;

    /**
     * 支付总金额
     */
    private BigDecimal orderAmount;

}
