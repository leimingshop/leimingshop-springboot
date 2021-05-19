/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单实体
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2019-06-13
 */
@Data
@ApiModel(description = "OrderDTO")
public class OrderDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;
    @ApiModelProperty(value = "订单编号")
    private Long orderSn;
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
    @ApiModelProperty(value = "买家等级ID")
    private Long buyerGraderId;
    @ApiModelProperty(value = "买家等级名称")
    private String buyerGraderName;
    @ApiModelProperty(value = "订单来源平台（0:pc,1:h5,2:android,3:ios,4:微信,5:小程序）")
    private Integer orderPlatform;
    @ApiModelProperty(value = "运费价格")
    private BigDecimal shippingFee;
    @ApiModelProperty(value = "优惠卷总金额")
    private BigDecimal couponAmount;
    @ApiModelProperty(value = "满减活动总金额")
    private BigDecimal reduceAmount;
    @ApiModelProperty(value = "优惠总金额")
    private BigDecimal preferentialPrice;
    @ApiModelProperty(value = "商品总价格")
    private BigDecimal goodsAmount;
    @ApiModelProperty(value = "支付总金额")
    private BigDecimal orderAmount;
    @ApiModelProperty(value = "余额支付总金额")
    private BigDecimal balanceAmount;
    @ApiModelProperty(value = "订单留言")
    private String orderMessage;
    @ApiModelProperty(value = "订单优惠券活动json：{店铺id : 优惠券id}")
    private String memberCouponsId;
    @ApiModelProperty(value = "订单状态：0:已取消;10:待付款;20:待发货;30:待收货;40:交易完成;")
    private Integer orderStatus;
    @ApiModelProperty(value = "前台状态：0:已取消;10:待付款;20:待发货;30:待收货;40:交易完成;")
    private Integer appStatus;
    @ApiModelProperty(value = "支付方式ID")
    private Long paymentId;
    @ApiModelProperty(value = "支付方式名称")
    private String paymentName;
    @ApiModelProperty(value = "订单支付ID")
    private Long payId;
    @ApiModelProperty(value = "订单支付编号")
    private Long paySn;
    @ApiModelProperty(value = "付款状态:0:未付款;1:已付款")
    private Integer paymentStatus;
    @ApiModelProperty(value = "支付时间")
    private Date paymentTime;
    @ApiModelProperty(value = "支付留言")
    private String paymentMessage;
    @ApiModelProperty(value = "外部支付订单编号")
    private String outSn;
    @ApiModelProperty(value = "第三方交易流水号")
    private String tradeSn;
    @ApiModelProperty(value = "订单完成时间")
    private Date completeTime;
    @ApiModelProperty(value = "用户提交抬头类型（默认0：不开票、1：个人、2：单位）")
    private Integer invoiceFlag;
    @ApiModelProperty(value = "退换货标记（默认0：没有退换货，1：有退换货）")
    private Integer afterFlag;
    @ApiModelProperty(value = "发货地址ID")
    private Long daddressId;
    @ApiModelProperty(value = "发货备注")
    private String deliverExplain;
    @ApiModelProperty(value = "收货地址ID")
    private Long addressId;
    @ApiModelProperty(value = "配送时间")
    private Date transportTime;
    @ApiModelProperty(value = "配送方式 0:无需物流 1:快递 2自提 3电子提货码")
    private Integer devlierType;
    @ApiModelProperty(value = "配送公司ID")
    private Long transportCompanyId;
    @ApiModelProperty(value = "配送公司编号")
    private String trandportExpressCode;
    @ApiModelProperty(value = "配送公司名称")
    private String transportCompanyName;
    @ApiModelProperty(value = "配送公司电话")
    private String transportCompanyPhone;
    @ApiModelProperty(value = "物流单号")
    private String transportCode;
    @ApiModelProperty(value = "评价状态 0为评价，1已评价")
    private Integer evaluateStatus;
    @ApiModelProperty(value = "评价时间")
    private Date evaluateTime;
    @ApiModelProperty(value = "结算状态:0,未结算,1已结算")
    private Integer balanceStatus;
    @ApiModelProperty(value = "结算时间")
    private Date balanceTime;
    @ApiModelProperty(value = "取消原因ID（原因描述表ID）")
    private Long causeId;
    @ApiModelProperty(value = "订单取消原因")
    private String cancelCause;
    @ApiModelProperty(value = "订单取消时间")
    private Date cancelDate;
    @ApiModelProperty(value = "是否拆单（默认0:否,1:是）")
    private Integer isSplit;
    @ApiModelProperty(value = "父订单号")
    private Long parentOrderSn;
    @ApiModelProperty(value = "创建时间")
    private Date createDate;

    @ApiModelProperty(value = "商家备注等级")
    private Integer sellerRemarkGrade;
    @ApiModelProperty(value = "商家备注")
    private String sellerRemark;

    @ApiModelProperty(value = "订单赠送积分")
    private Integer orderPointsCount;

    @ApiModelProperty(value = "订单赠送成长值")
    private Integer orderGrowthCount;

    @ApiModelProperty(value = "拼团记录id")
    private Long groupRecordId;

    @ApiModelProperty(value = "拼团成团超时时间")
    private Date groupOverTime;

    @ApiModelProperty(value = "拼团状态(0:进行中，1:成功，2:失败)")
    private Integer groupStatus;

    @ApiModelProperty(value = "拼团所需人数")
    private Integer groupNeedNum;

    @ApiModelProperty(value = "虚拟订单标记（0:否，1是)")
    private Integer virtualFlag;

    @ApiModelProperty(value = "余额已退款金额")
    private BigDecimal balanceRefundAmount;
}
