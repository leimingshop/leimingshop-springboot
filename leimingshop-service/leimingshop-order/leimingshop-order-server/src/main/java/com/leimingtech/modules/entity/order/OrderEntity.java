/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.order;

import com.baomidou.mybatisplus.annotation.*;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_order")
public class OrderEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 订单编号
     */
    private Long orderSn;

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
     * 买家等级ID
     */
    private Long buyerGraderId;

    /**
     * 买家等级名称
     */
    private String buyerGraderName;

    /**
     * 订单来源平台（0:pc,1:h5,2:android,3:ios,4:微信,5:小程序）
     */
    private Integer orderPlatform;

    /**
     * 运费价格
     */
    private BigDecimal shippingFee;

    /**
     * 优惠卷总金额
     */
    private BigDecimal couponAmount;

    /**
     * 满减活动总金额
     */
    private BigDecimal reduceAmount;

    /**
     * 优惠总金额
     */
    private BigDecimal preferentialPrice;

    /**
     * 商品总价格
     */
    private BigDecimal goodsAmount;

    /**
     * 支付总金额
     */
    private BigDecimal orderAmount;

    /**
     * 订单优惠券活动json：{店铺id : 优惠券id}
     */
    private String memberCouponsId;

    /**
     * 订单留言
     */
    private String orderMessage;

    /**
     * 订单状态：0:已取消;10:待付款;20:待发货;30:待收货;40:交易完成;
     */
    private Integer orderStatus;
    /**
     * 前台状态：0:已取消;10:待付款;20:待发货;30:待收货;40:交易完成;
     */
    private Integer appStatus;
    /**
     * 支付方式ID
     */
    private Long paymentId;

    /**
     * 支付方式名称
     */
    private String paymentName;

    /**
     * 支付标识
     */
    private String paymentCode;

    /**
     * 订单支付ID
     */
    private Long payId;

    /**
     * 订单支付编号
     */
    private Long paySn;

    /**
     * 付款状态:0:未付款;1:已付款
     */
    private Integer paymentStatus;

    /**
     * 支付时间
     */
    private Date paymentTime;

    /**
     * 支付留言
     */
    private String paymentMessage;

    /**
     * 外部支付订单编号
     */
    private String outSn;

    /**
     * 第三方交易流水号
     */
    private String tradeSn;

    /**
     * 订单完成时间
     */
    private Date completeTime;
    /**
     * 用户提交抬头类型（默认0：不开票、1：个人、2：单位）
     */
    private Integer invoiceFlag;
    /**
     * 退换货标记（默认0：没有退换货，1：有退换货）
     */
    private Integer afterFlag;
    /**
     * 发货地址ID
     */
    private Long daddressId;

    /**
     * 发货备注
     */
    private String deliverExplain;

    /**
     * 收货地址ID
     */
    private Long addressId;

    /**
     * 配送时间
     */
    private Date transportTime;

    /**
     * 配送方式 0:无需物流 1:快递 2自提 3电子提货码
     */
    private Integer devlierType;

    /**
     * 配送公司ID
     */
    private Long transportCompanyId;

    /**
     * 配送公司编号
     */
    private String trandportExpressCode;

    /**
     * 配送公司名称
     */
    private String transportCompanyName;

    /**
     * 物流单号
     */
    private String transportCode;

    /**
     * 配送公司电话
     */
    private String transportCompanyPhone;

    /**
     * 评价状态 0为评价，1已评价
     */
    private Integer evaluateStatus;

    /**
     * 评价时间
     */
    private Date evaluateTime;

    /**
     * 结算状态:0,未结算,1已结算
     */
    private Integer balanceStatus;

    /**
     * 结算时间
     */
    private Date balanceTime;

    /**
     * 取消原因ID（原因描述表ID）
     */
    private Long causeId;

    /**
     * 订单取消原因
     */
    private String cancelCause;

    /**
     * 订单取消时间
     */
    private Date cancelDate;

    /**
     * 是否拆单（默认0:否,1:是）
     */
    private Integer isSplit;

    /**
     * 父订单号
     */
    private Long parentOrderSn;

    /**
     * 用户是否删除订单 0未删除 1已删除
     */
    private Integer showFlag;

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
     * 商家备注等级
     */
    private Integer sellerRemarkGrade;
    /**
     * 商家备注
     */
    private String sellerRemark;

    /**
     * 订单赠送积分
     */
    private Integer orderPointsCount;

    /**
     * 订单赠送成长值
     */
    private Integer orderGrowthCount;

    /**
     * 拼团记录id
     */
    private Long groupRecordId;

    /**
     * 拼团成团超时时间
     */
    private Date groupOverTime;

    /**
     * 拼团状态(0:进行中，1:成功，2:失败)
     */
    private Integer groupStatus;

    /**
     * 拼团所需人数
     */
    private Integer groupNeedNum;

    /**
     * 虚拟订单标记（0:否，1是）
     */
    private Integer virtualFlag;

    /**
     * 余额支付金额
     */
    private BigDecimal balanceAmount;

    /**
     * 余额已退款金额
     */
    private BigDecimal balanceRefundAmount;
}
