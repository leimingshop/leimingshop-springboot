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
 * @Description: 订单商品确认实体
 * @Date: 2019/6/22 11:32
 * @Version: V1.0
 */
@Data
@ApiModel(description = "OrderGoodsConfirmDTO")
public class OrderGoodsConfirmDTO implements Serializable {

    private static final long serialVersionUID = -2721144200159890288L;

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "订单确认表ID")
    private Long orderConfirmId;

    @ApiModelProperty(value = "商品ID")
    private Long goodsId;

    @ApiModelProperty(value = "规格ID")
    private Long specId;

    @ApiModelProperty(value = "商品数量")
    private Integer goodsNum;

    @ApiModelProperty(value = "商品图片")
    private String goodsImage;

    @ApiModelProperty(value = "店铺ID")
    private Long storeId;

    @ApiModelProperty(value = "店铺名称")
    private String storeName;

    @ApiModelProperty(value = "商品实际成交价")
    private BigDecimal goodsPayPrice;

    @ApiModelProperty(value = "商品销售价")
    private BigDecimal specPrice;

    @ApiModelProperty(value = "买家ID")
    private Long buyerId;

    @ApiModelProperty(value = "是否赠送商品(0:否，1:是)")
    private Integer isGift;

    @ApiModelProperty(value = "商品成本价")
    private BigDecimal specCostPrice;

    @ApiModelProperty("满减活动id")
    private Long reduceActivityId;

    @ApiModelProperty(value = "活动id")
    private Long activityId;

    @ApiModelProperty(value = "活动类型 0无活动 1优惠券 2满减 3秒杀 4拼团 5限时抢购")
    private Integer activityType;

    @ApiModelProperty(value = "活动记录id")
    private Long activityRecordId;

    @ApiModelProperty(value = "虚拟订单标记（0:否，1是)")
    private Integer virtualFlag;

    @ApiModelProperty(value = "配送方式 0:无需物流 1:快递 2自提 3电子提货码")
    private Integer devlierType;

}
