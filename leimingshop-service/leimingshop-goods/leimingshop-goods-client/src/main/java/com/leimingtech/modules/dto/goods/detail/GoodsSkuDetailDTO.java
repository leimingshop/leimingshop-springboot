/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.goods.detail;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author: xuzhch
 * @Description: 商品规格管理
 * @Date :2019/6/4 17:42
 * @Version V1.0
 **/
@Data
@ApiModel(description = "GoodsSkuDetailDTO")
public class GoodsSkuDetailDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 规格id
     */
    @ApiModelProperty(value = "规格id")
    private Long id;

    /**
     * 商品ID
     */
    @ApiModelProperty(value = "商品ID")
    private Long goodsId;

    /**
     * 商品规格名称
     */
    @ApiModelProperty(value = "商品规格名称")
    private String specName;

    @ApiModelProperty(value = "商品分类一级名称")
    private String firstGcName;

    @ApiModelProperty(value = "商品分类二级名称")
    private String secondGcName;

    @ApiModelProperty(value = "商品分类三级名称")
    private String thirdGcName;

    /**
     * 商品规格名称
     */
    @ApiModelProperty(value = "规格属性值名称")
    private String specAttrName;

    /**
     * 规格库存
     */
    @ApiModelProperty(value = "规格库存")
    private Integer specStorage;
    /**
     * 销售价
     */
    @ApiModelProperty(value = "销售价")
    private BigDecimal specSellPrice;
    /**
     * 成本价
     */
    @ApiModelProperty(value = "成本价")
    private BigDecimal specCostPrice;

    @ApiModelProperty(value = "ID")
    private Long specAttrValueId;

}