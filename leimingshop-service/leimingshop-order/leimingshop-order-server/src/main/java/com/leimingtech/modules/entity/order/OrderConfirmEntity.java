/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.order;

import com.baomidou.mybatisplus.annotation.*;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: LX 17839193044@162.com
 * @Description: 订单确定Entity
 * @Date: 2019/6/22 11:27
 * @Version: V1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_order_confirm")
public class OrderConfirmEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 店铺ID
     */
    private Long storeId;

    /**
     * 店铺名称
     */
    private String storeName;

    /**
     * 买家ID
     */
    private Long buyerId;

    /**
     * 买家名称
     */
    private String buyerName;

    /**
     * 买家邮箱
     */
    private String buyerEmail;

    /**
     * 订单来源平台（0:pc,1:h5,2:android,3:ios,4:微信,5:小程序）
     */
    private Integer orderPlatform;

    /**
     * 商品总价格
     */
    private BigDecimal goodsAmount;

    /**
     * 优惠总金额
     */
    private BigDecimal preferentialPrice;

    /**
     * 支付总金额
     */
    private BigDecimal orderAmount;

    /**
     * 余额支付金额
     */
    private BigDecimal balanceAmount;

    /**
     * 运费价格
     */
    private BigDecimal shippingFee;

    /**
     * 运费金额
     */
    private String shippingAmount;

    /**
     * 订单留言
     */
    private String orderMessage;

    @ApiModelProperty(value = "订单优惠券活动json：{店铺id : 优惠券id}")
    private String memberCouponsId;

    /**
     * 收货地址ID
     */
    private Long addressId;

    /**
     * 虚拟订单标记（0:否，1是）
     */
    private Integer virtualFlag;

    /**
     * 收件人(虚拟订单必传)）
     */
    private String virtualCustomer;

    /**
     * 用户手机号(虚拟订单必传)
     */
    private String virtualPhone;

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

    /**
     * 用户等级ID
     */
    private Long buyerGraderId;

    /**
     * 用户等级名称
     */
    private String buyerGraderName;

}
