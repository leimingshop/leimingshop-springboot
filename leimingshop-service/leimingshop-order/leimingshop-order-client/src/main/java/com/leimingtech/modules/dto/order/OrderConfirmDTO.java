/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author: LX 17839193044@162.com
 * @Description: 订单确认DTO
 * @Date: 2019/6/22 11:32
 * @Version: V1.0
 */
@Data
@ApiModel(description = "OrderConfirmDTO")
public class OrderConfirmDTO implements Serializable {

    private static final long serialVersionUID = -6825528672598019672L;

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "店铺ID")
    private Long storeId;

    @ApiModelProperty(value = "店铺名称")
    private String storeName;

    @ApiModelProperty(value = "买家ID")
    private Long buyerId;

    @ApiModelProperty(value = "买家名称")
    private String buyerName;

    @ApiModelProperty(value = "买家邮箱")
    private String buyerEmail;

    @ApiModelProperty(value = "订单来源平台（0:pc,1:h5,2:android,3:ios,4:微信,5:小程序）")
    private Integer orderPlatform;

    @ApiModelProperty(value = "商品总价格")
    private BigDecimal goodsAmount;

    @ApiModelProperty(value = "优惠总金额")
    private BigDecimal preferentialPrice;

    @ApiModelProperty(value = "支付总金额")
    private BigDecimal orderAmount;

    @ApiModelProperty(value = "余额支付金额")
    private BigDecimal balanceAmount;

    @ApiModelProperty(value = "运费价格")
    private BigDecimal shippingFee;

    @ApiModelProperty(value = "运费金额")
    private String shippingAmount;

    @ApiModelProperty(value = "订单留言")
    private String orderMessage;

    @ApiModelProperty(value = "订单优惠券活动json：{店铺id : 优惠券id}")
    private String memberCouponsId;

    @ApiModelProperty(value = "收货地址ID")
    private Long addressId;

    @ApiModelProperty(value = "用户等级ID")
    private Long buyerGraderId;

    @ApiModelProperty(value = "用户等级名称")
    private String buyerGraderName;

    @ApiModelProperty(value = "虚拟订单标记（0:否，1是)")
    private Integer virtualFlag;

    @ApiModelProperty(value = "收件人(虚拟订单必传)")
    private String virtualCustomer;

    @ApiModelProperty(value = "用户手机号(虚拟订单必传)")
    private String virtualPhone;


}
