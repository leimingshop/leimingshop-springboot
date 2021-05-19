/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.goods.spec;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author: weixianchun
 * @Description: 商品规格管理(修改用)
 * @Date :2019/6/4 17:42
 * @Version V1.0
 **/
@Data
@ApiModel(description = "GoodsSpecUpdateDTO")
public class GoodsSpecUpdateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @ApiModelProperty(value = "ID")
    private Long id;
    /**
     * 商品规格编号
     */
    @ApiModelProperty(value = "商品规格编号")
    private String specSerial;
    /**
     * 商品规格名称
     */
    @ApiModelProperty(value = "商品规格名称")
    private String specName;
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
    /**
     * 商品规格属性值名称
     */
    @ApiModelProperty(value = "商品规格属性值名称（中间用逗号隔开）")
    private String specAttrName;
    /**
     * 是否启用（默认0启用，1未启用）
     */
    @ApiModelProperty(value = "是否启用（默认0启用，1未启用）")
    private Integer isEnable;
    /**
     * 规格上下架状态（默认:2未上架,0下架状态，1上架状态）
     */
    @ApiModelProperty(value = "规格上下架状态（默认:2未上架,0下架状态，1上架状态）")
    private Integer specShow;

}