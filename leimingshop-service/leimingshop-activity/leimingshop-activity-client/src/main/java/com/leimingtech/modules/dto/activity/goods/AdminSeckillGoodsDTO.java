/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.activity.goods;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 后台秒杀已选择商品列表实体
 *
 * @author 刘远杰 2634443725@qq.com
 * @since v1.0.0 2020-03-11
 */
@Data
@ApiModel(value = "后台秒杀已选择商品列表实体")
public class AdminSeckillGoodsDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;
    @ApiModelProperty(value = "规格ID")
    private Long specId;

    @ApiModelProperty(value = "商品spu id")
    private Long goodsId;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "商品主图")
    private String goodsMainPicture;

    @ApiModelProperty(value = "销售价")
    private BigDecimal specSellPrice;

    @ApiModelProperty(value = "活动库存")
    private Integer activityStorage;

    @ApiModelProperty(value = "活动剩余库存")
    private Integer activitySurplusStorage;

    @ApiModelProperty(value = "最小活动价格")
    private BigDecimal minActivityPrice;

    @ApiModelProperty(value = "最大活动价格")
    private BigDecimal maxActivityPrice;

    @ApiModelProperty(value = "活动价格")
    private BigDecimal activityPrice;

    @ApiModelProperty(value = "spu订单数（包含已删除的sku下单数）")
    private Integer spuOrderNum;

    public BigDecimal getActivityPrice() {
        if (minActivityPrice != null && maxActivityPrice != null && minActivityPrice.compareTo(maxActivityPrice) == 0) {
            return minActivityPrice;
        } else {
            return null;
        }
    }

}
