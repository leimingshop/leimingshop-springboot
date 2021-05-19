/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.group;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 拼团已选择商品列表实体
 */
@Data
@ApiModel(description = "GroupGoodsDTO")
public class GroupGoodsDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;
    @ApiModelProperty(value = "规格ID")
    private Long specId;

    @ApiModelProperty(value = "商品spu id")
    private Long goodsId;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

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

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "成团人数")
    private Integer regimentNum;

    @ApiModelProperty(value = "参团次数限制")
    private Integer onceBuyLimit;

    @ApiModelProperty(value = "单次购买数量")
    private Integer joinLimit;

    @ApiModelProperty(value = "商品主图")
    private String goodsMainPicture;

    public BigDecimal getActivityPrice() {
        if (minActivityPrice != null && maxActivityPrice != null && minActivityPrice.compareTo(maxActivityPrice) == 0) {
            return minActivityPrice;
        } else {
            return null;
        }
    }

}
