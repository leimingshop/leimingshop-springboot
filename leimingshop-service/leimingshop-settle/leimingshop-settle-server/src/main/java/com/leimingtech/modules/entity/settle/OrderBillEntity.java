/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.settle;

import com.baomidou.mybatisplus.annotation.TableName;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单结算表
 *
 * @author chengqian chengqian@leimingtech.com
 * @since v1.0.0 2019-12-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_order_bill")
public class OrderBillEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 结算单编号
     */
    private Long billTotalId;
    /**
     * 订单编号
     */
    private Long orderSn;
    /**
     * 订单ID
     */
    private Long orderId;
    /**
     * 交易时间
     */
    private Date payTime;
    /**
     * 订单金额
     */
    private BigDecimal orderTotals;
    /**
     * 佣金金额
     */
    private BigDecimal commisTotals;
    /**
     * 店铺促销活动费用
     */
    private BigDecimal storeCostTotals;
    /**
     * 积分抵扣金额
     */
    private BigDecimal pointAmount;
    /**
     * 支付方式
     */
    private String paymentName;
}