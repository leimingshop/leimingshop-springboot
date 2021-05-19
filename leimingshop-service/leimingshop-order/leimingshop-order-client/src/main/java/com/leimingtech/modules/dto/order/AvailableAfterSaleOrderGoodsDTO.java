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
 * 可申请售后订单商品实体
 *
 * @author: SWH ab4856812@163.com
 * @date: 2019/6/17 18:38
 * @version: V1.0
 */
@Data
@ApiModel(description = "AvailableAfterSaleOrderGoodsDTO")
public class AvailableAfterSaleOrderGoodsDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "订单ID")
    private Long orderId;

    @ApiModelProperty(value = "订单编号")
    private Long orderSn;

    @ApiModelProperty(value = "买家ID")
    private Long buyerId;

    @ApiModelProperty(value = "店铺ID")
    private Long storeId;

    @ApiModelProperty(value = "店铺名称")
    private String storeName;

    @ApiModelProperty(value = "商品ID")
    private Long goodsId;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "规格ID")
    private Long specId;

    @ApiModelProperty(value = "规格ID")
    private String specSerial;

    @ApiModelProperty(value = "规格描述")
    private String specInfo;

    @ApiModelProperty(value = "商品规格属性和属性值名称（key:value）")
    private String specAttrValueName;

    @ApiModelProperty(value = "商品规格价格 ")
    private BigDecimal specPrice;

    @ApiModelProperty(value = "商品规格实际成交价")
    private BigDecimal specPayPrice;

    @ApiModelProperty(value = "商品数量")
    private Integer goodsNum;

    @ApiModelProperty(value = "商品图片")
    private String goodsImage;

    @ApiModelProperty(value = "可申请售后数量")
    private Integer aftersaleQuantity;

    @ApiModelProperty(value = "是否赠送商品(0:否，1:是)")
    private Integer isGift;

    @ApiModelProperty(value = "订单完成时间")
    private Date completeTime;

    @ApiModelProperty(value = "是否可申请退货(0:否，1:是)")
    private Integer isReturn;

    @ApiModelProperty(value = "不可申请退货原因")
    private String notReturnReson;

    @ApiModelProperty(value = "是否可申请换货(0:否，1:是)")
    private Integer isBarter;

    @ApiModelProperty(value = "不可申请换货原因")
    private String notBarterReson;

    @ApiModelProperty(value = "联系人")
    private String contacts;

    @ApiModelProperty(value = "联系人电话")
    private String contactsPhone;

    @ApiModelProperty(value = "省级ID")
    private Long provinceId;
    @ApiModelProperty(value = "市级ID")
    private Long cityId;
    @ApiModelProperty(value = "地区ID")
    private Long areaId;
    @ApiModelProperty(value = "街道ID")
    private Long stressId;
    @ApiModelProperty(value = "地址")
    private String address;
    @ApiModelProperty(value = "地区内容")
    private String areaInfo;

}
