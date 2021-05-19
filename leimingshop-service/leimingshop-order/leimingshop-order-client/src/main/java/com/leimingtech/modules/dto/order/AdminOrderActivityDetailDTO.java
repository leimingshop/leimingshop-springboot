/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 功能描述：
 * <订单详情及活动信息实体>
 *
 * @author 刘远杰
 * @version 1.0
 * @Date 2019/6/14 15:48
 **/
@Data
@ApiModel(description = "AdminOrderActivityDetailDTO")
public class AdminOrderActivityDetailDTO {

    @ApiModelProperty(value = "主键")
    private Long id;
    @ApiModelProperty(value = "订单编号")
    private Long orderSn;
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

    @ApiModelProperty(value = "创建时间")
    private Date createDate;
    @ApiModelProperty(value = "商品总数")
    private Integer totalNum;

    @ApiModelProperty(value = "订单商品集合")
    private List<OrderGoodsDTO> orderGoodsList;

    @ApiModelProperty(value = "订单促销集合")
    private List<AdminOrderActivityDTO> adminOrderActivityList;

    @ApiModelProperty(value = "订单优惠券集合")
    private List<AdminOrderCouponsDTO> adminOrderCouponsList;
}
