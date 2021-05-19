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
 * @Description: 订单支付管理
 * @Date :2019/6/18 10:25
 * @Version V1.0
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_order_pay")
public class OrderPayEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 支付单号
     */
    private Long paySn;

    /**
     * 订单编号
     */
    private Long orderSn;

    /**
     * 买家ID
     */
    private Long buyerId;

    /**
     * 买家名称
     */
    private String buyerName;

    /**
     * 0默认未支付1已支付(只有第三方支付接口通知到时才会更改此状态)2已取消
     */
    private Integer payState;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 第三方支付总金额
     */
    private BigDecimal payAmount;

    /**
     * 余额支付金额
     */
    private BigDecimal balanceAmount;

    /**
     * 支付总金额
     */
    private BigDecimal payTotalAmount;

    /**
     * 第三方交易单号
     */
    private String transactionId;

    /**
     * 支付自动取消时间
     */
    private Date cancalDate;

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
