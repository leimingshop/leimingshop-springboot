/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.aftersale.dto;

import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 售后商品表
 *
 * @author huangkeyuan
 * @email huangkeyuan@leimingtech.dao
 * @since 1.0.0 2019-06-13
 */
@Data
@ApiModel(description = "AftersaleGoodsDTO")
public class AftersaleGoodsDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "{id.require}", groups = UpdateGroup.class)
    @ApiModelProperty(value = "主键")
    private Long id;
    @ApiModelProperty(value = "售后单号")
    private Long aftersaleSn;
    @ApiModelProperty(value = "订单商品ID")
    private Long orderGoodsId;
    @ApiModelProperty(value = "商品ID")
    private Long goodsId;
    @ApiModelProperty(value = "商品名称")
    private String goodsName;
    @ApiModelProperty(value = "商品数量")
    private Integer goodsNum;
    @ApiModelProperty(value = "商品规格ID")
    private Long specId;
    @ApiModelProperty(value = "商品规格货号")
    private String specSerial;
    @ApiModelProperty(value = "商品规格名称")
    private String specName;
    @ApiModelProperty(value = "商品规格属性值名称")
    private String specAttrName;
    @ApiModelProperty(value = "主商品规格主图")
    private String specMainPicture;
    @ApiModelProperty(value = "商品实际支付金额")
    private BigDecimal specPayPrice;
    @ApiModelProperty(value = "商品售价")
    private BigDecimal specSellPrice;
    @ApiModelProperty(value = "商品成本价")
    private BigDecimal specCostPrice;
    @ApiModelProperty(value = "是否是赠送商品(0:否，1:是)")
    private Integer isGift;

}