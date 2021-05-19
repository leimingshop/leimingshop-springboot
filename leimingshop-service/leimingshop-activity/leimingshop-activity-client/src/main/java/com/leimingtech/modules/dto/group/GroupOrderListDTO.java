/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.group;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 拼团订单详情信息
 *
 * @author huangkeyuan@leimingtech.com
 * @return
 * @date 2020-03-17 09:56
 **/
@Data
@ApiModel(description = "GroupOrderListDTO")
public class GroupOrderListDTO {

    @ApiModelProperty(value = "订单id")
    private Long id;

    @ApiModelProperty(value = "订单编号")
    private Long orderSn;

    @ApiModelProperty(value = "买家名称")
    private String buyerName;

    @ApiModelProperty(value = "支付总金额")
    private BigDecimal orderAmount;

    @ApiModelProperty(value = "订单状态：0:已取消;10:待付款;20:待发货;30:待收货;40:交易完成;")
    private Integer orderStatus;

    @ApiModelProperty(value = "前台状态：0:已取消;10:待付款;20:待发货;30:待收货;40:交易完成;")
    private Integer appStatus;

    @ApiModelProperty(value = "支付方式名称")
    private String paymentName;

    @ApiModelProperty(value = "支付时间")
    private Date paymentTime;

    @ApiModelProperty("规格ID")
    private Long specId;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "spu名称")
    private String spuName;

    @ApiModelProperty(value = "规格描述")
    private String specInfo;

    @ApiModelProperty(value = "商品规格价格 ")
    private BigDecimal specPrice;

    @ApiModelProperty(value = "商品规格实际成交价")
    private BigDecimal specPayPrice;

    @ApiModelProperty(value = "商品数量")
    private Integer goodsNum;

    @ApiModelProperty(value = "商品图片")
    private String goodsImage;

    @ApiModelProperty(value = "备注信息")
    private String sellerRemark;

}
