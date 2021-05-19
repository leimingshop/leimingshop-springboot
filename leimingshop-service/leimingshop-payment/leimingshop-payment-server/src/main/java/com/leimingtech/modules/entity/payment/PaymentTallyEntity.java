/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.payment;

import com.baomidou.mybatisplus.annotation.*;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: weixianchun
 * @Description: 支付流水管理
 * @Date :2019/6/18 11:55
 * @Version V1.0
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_payment_tally")
public class PaymentTallyEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 店铺id
     */
    private Long storeId;

    /**
     * 订单编号（支付记录为父订单编号，退款记录为子订单编号）
     */
    private Long orderSn;

    /**
     * 退款编号
     */
    private Long aftersaleSn;

    /**
     * 收支标识（0:收入，1:支出）
     */
    private Integer transactionStatus;

    /**
     * 支付方式代码
     */
    private String paymentCode;

    /**
     * 支付方式名称
     */
    private String paymentName;

    /**
     * 支付状态:1,成功;2,失败;
     */
    private Integer paymentStatus;

    /**
     * 交易编号(商城内部交易单号)
     */
    private Long paymentSn;

    /**
     * 1:PC;2:APP;3:h5
     */
    private Integer paymentFrom;

    /**
     * 交易金额
     */
    private BigDecimal paymentAmount;

    /**
     * 余额支付金额
     */
    private BigDecimal balanceAmount;

    /**
     * 交易类型:10,订单支付;20,充值
     */
    private Integer tradeType;

    /**
     * 交易流水号
     */
    private String tradeSn;

    /**
     * 买家id
     */
    private Long buyerId;

    /**
     * 买家名称
     */
    private String buyerName;

    /**
     * 交易备注
     */
    private String paymentRemarks;

    /**
     * 是否拆单（默认0:否,1:是）
     */
    private Integer isSplit;

    /**
     * 更新人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updater;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateDate;

    /**
     * 删除标记（默认为0未删除，1已删除）
     */
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Integer delFlag;

    /**
     * 乐观锁
     */
    @Version
    private Integer version;

}
