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
 * 对账汇总表
 *
 * @author chengqian chengqian@leimingtech.com
 * @since v1.0.0 2019-12-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_bill_total")
public class BillTotalEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 对账汇总单编号
     */
    private String billTotalSn;
    /**
     * 店铺ID
     */
    private Long storeId;
    /**
     * 店铺名
     */
    private String storeName;
    /**
     * 本期应结金额
     */
    private BigDecimal resultTotals;
    /**
     * 店铺类型(1 自营2 普通)
     */
    private Integer storeType;
    /**
     * 商家确认状态（0 未确认 1 已确认）
     */
    private Integer confirmStatus;
    /**
     * 结算状态 0未结算1已结算
     */
    private Integer billState;
    /**
     * 开始日期
     */
    private Date obtStartTime;
    /**
     * 结束日期
     */
    private Date obtEndTime;
    /**
     * 出账日期
     */
    private Date billTime;
    /**
     * 结算备注
     */
    private String billRemark;
    /**
     * 商家备注
     */
    private String storeRemark;

    /**
     * 佣金总金额
     */
    private BigDecimal commisTotal;

    /**
     * 退还总佣金
     */
    private BigDecimal returnCommisTotal;
    /**
     * 店铺活动总费用
     */
    private BigDecimal storeCostTotal;
    /**
     * 积分抵扣总金额
     */
    private BigDecimal pointTotal;

    /**
     * 退款总金额
     */
    private BigDecimal returnAmount;

    /**
     * 订单总金额
     */
    private BigDecimal orderTotalAmount;
}