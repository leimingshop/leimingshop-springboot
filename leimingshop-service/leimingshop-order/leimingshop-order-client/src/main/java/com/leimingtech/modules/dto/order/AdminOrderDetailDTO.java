/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.order;

import com.leimingtech.modules.dto.invoice.OrderInvoiceDTO;
import com.leimingtech.modules.dto.virtual.FetchCodeDTO;
import com.leimingtech.modules.vo.order.PCOrderPageVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 功能描述：
 * <订单详情信息实体>
 *
 * @author 刘远杰
 * @version 1.0
 * @Date 2019/6/14 15:48
 **/
@Data
@ApiModel(description = "AdminOrderDetailDTO")
public class AdminOrderDetailDTO {

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
    @ApiModelProperty(value = "收件人名称")
    private String trueName;
    @ApiModelProperty(value = "买家邮箱")
    private String buyerEmail;
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
    @ApiModelProperty(value = "订单优惠券活动json：{店铺id : 优惠券id}")
    private String memberCouponsId;
    @ApiModelProperty(value = "订单留言")
    private String orderMessage;
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
    @ApiModelProperty(value = "配送方式")
    private Integer devlierType;
    @ApiModelProperty(value = "配送公司ID")
    private Long transportCompanyId;
    @ApiModelProperty(value = "配送公司编号")
    private String trandportExpressCode;
    @ApiModelProperty(value = "配送公司名称")
    private String transportCompanyName;
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
    @ApiModelProperty(value = "订单取消原因")
    private String cancelCause;
    @ApiModelProperty(value = "是否拆单（默认0:否,1:是）")
    private Integer isSplit;
    @ApiModelProperty(value = "父订单号")
    private Long parentOrderSn;
    @ApiModelProperty(value = "支付自动取消时间/订单取消时间")
    private Date cancelDate;

    @ApiModelProperty(value = "创建时间")
    private Date createDate;
    @ApiModelProperty(value = "商品总数")
    private Integer totalNum;
    @ApiModelProperty(value = "是否备注 0未备注 1已备注")
    private Integer disable;
    @ApiModelProperty(value = "商家备注等级(1,2,3,4)")
    private Integer sellerRemarkGrade;
    @ApiModelProperty(value = "商家备注")
    private String sellerRemark;
    @ApiModelProperty(value = "会员等级id")
    private Long buyerGraderId;
    @ApiModelProperty(value = "会员等级名称")
    private String buyerGraderName;
    @ApiModelProperty(value = "拼团记录id")
    private Long groupRecordId;

    @ApiModelProperty(value = "拼团成团超时时间")
    private Date groupOverTime;

    @ApiModelProperty(value = "拼团状态(0:进行中，1:成功，2:失败)")
    private Integer groupStatus;

    @ApiModelProperty(value = "拼团所需人数")
    private Integer groupNeedNum;

    @ApiModelProperty(value = "虚拟订单标记（0:否，1是）")
    private Integer virtualFlag;

    @ApiModelProperty(value = "订单发票信息")
    private OrderInvoiceDTO orderInvoiceDTO;

    @ApiModelProperty(value = "订单日志记录")
    private List<OrderLogDTO> orderLogDTOList;

    @ApiModelProperty(value = "订单商品集合")
    private List<OrderGoodsDTO> orderGoodsDTOList;

    @ApiModelProperty(value = "订单物流信息记录集合")
    private OrderLogisticsDTO orderLogisticsDTO;

    @ApiModelProperty(value = "订单收货地址信息")
    private OrderAddressDTO orderAddressDTO;

    @ApiModelProperty(value = "订单操作时间集合")
    private List<AdminOrderTimeDTO> orderTimeDTOList;

    @ApiModelProperty(value = "电子提货码集合")
    private List<FetchCodeDTO> fetchCodeDTOList;

    @ApiModelProperty(value = "子订单集合")
    private List<PCOrderPageVO> orderChildrenList;
}
