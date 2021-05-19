/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.order;

import com.leimingtech.modules.dto.virtual.FetchCodeDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * H5订单详情实体
 *
 * @author lixiangx@leimigntech.com
 * @date 2019/11/6 17:41
 **/
@Data
@ApiModel(description = "H5OrderDetailDTO")
public class H5OrderDetailDTO implements Serializable {

    @ApiModelProperty(value = "主键")
    private Long id;
    @ApiModelProperty(value = "订单编号")
    private Long orderSn;
    @ApiModelProperty(value = "买家ID")
    private Long buyerId;
    @ApiModelProperty(value = "买家名称")
    private String buyerName;
    @ApiModelProperty(value = "运费价格")
    private BigDecimal shippingFee;
    @ApiModelProperty(value = "优惠卷总金额")
    private BigDecimal couponAmount;
    @ApiModelProperty(value = "满减总金额")
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
    @ApiModelProperty(value = "订单状态：0:已取消;10:待付款;20:待发货;30:待收货;40:交易完成;")
    private Integer orderStatus;
    @ApiModelProperty(value = "前台状态：0:已取消;10:待付款;20:待发货;30:待收货;40:交易完成;")
    private Integer appStatus;
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
    @ApiModelProperty(value = "订单完成时间")
    private Date completeTime;
    @ApiModelProperty(value = "发货备注")
    private String deliverExplain;
    @ApiModelProperty(value = "配送时间")
    private Date transportTime;
    @ApiModelProperty(value = "配送公司名称")
    private String transportCompanyName;
    @ApiModelProperty(value = "物流单号")
    private String transportCode;
    @ApiModelProperty(value = "评价状态 0为评价，1已评价")
    private Integer evaluateStatus;
    @ApiModelProperty(value = "评价时间")
    private Date evaluateTime;
    @ApiModelProperty(value = "订单取消原因")
    private String cancelCause;
    @ApiModelProperty(value = "是否拆单（默认0:否,1:是）")
    private Integer isSplit;
    @ApiModelProperty(value = "父订单号")
    private Long parentOrderSn;
    @ApiModelProperty(value = "支付自动取消时间")
    private Date cancelDate;
    @ApiModelProperty(value = "创建时间")
    private Date createDate;
    @ApiModelProperty(value = "商品总数")
    private Integer totalNum;
    @ApiModelProperty(value = "是否可申请售后 0不可申请 1可申请")
    private Integer isAfterSale;
    @ApiModelProperty(value = "抬头名称")
    private String company;
    @ApiModelProperty(value = "用户提交抬头类型（默认0：不开票、1：个人、2：单位）")
    private Integer invoiceFlag;
    @ApiModelProperty(value = "用户提交发票类型(1：电子、2：纸质、3：增值税)")
    private Integer invoiceType;
    @ApiModelProperty(value = "用户提交发票内容（1：商品明细、2：商品类别）")
    private Integer invoiceContent;

    @ApiModelProperty(value = "拼团记录id")
    private Long groupRecordId;

    @ApiModelProperty(value = "拼团成团超时时间")
    private Date groupOverTime;

    @ApiModelProperty(value = "拼团状态(0:进行中，1:成功，2:失败)")
    private Integer groupStatus;

    @ApiModelProperty(value = "拼团所需人数")
    private Integer groupNeedNum;

    @ApiModelProperty(value = "拼团用户头像集合")
    private List<String> groupMemberDTOList;

    @ApiModelProperty(value = "订单商品集合")
    private List<H5OrderDetailGoodsListDTO> orderDetailGoodsListDTOList;

    @ApiModelProperty(value = "订单收货地址信息")
    private OrderAddressDTO orderAddressDTO;

    @ApiModelProperty(value = "订单物流信息记录集合")
    private OrderLogisticsDTO orderLogisticsDTO;

    @ApiModelProperty("当前时间")
    private Long currentTime;

    @ApiModelProperty(value = "电子提货码集合")
    private List<FetchCodeDTO> fetchCodeDTOList;

    @ApiModelProperty(value = "配送方式 0:无需物流 1:快递 2自提 3电子提货码")
    private Integer devlierType;

    @ApiModelProperty(value = "是否投诉 0 未投诉 1已投诉")
    private Integer complainFlag;
}
