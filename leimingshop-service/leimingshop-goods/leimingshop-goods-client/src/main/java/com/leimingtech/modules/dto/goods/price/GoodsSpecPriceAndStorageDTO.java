/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.goods.price;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: weixianchun
 * @Description: 商品规格管理
 * @Date :2019/6/4 17:42
 * @Version V1.0
 **/
@Data
@ApiModel(description = "GoodsSpecPriceAndStorageDTO")
public class GoodsSpecPriceAndStorageDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 规格ID
     */
    @ApiModelProperty(value = "规格ID")
    private Long id;
    /**
     * 规格编号
     */
    @ApiModelProperty(value = "规格编号")
    private String specSerial;
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
     * 普通库存
     */
    @ApiModelProperty(value = "普通库存")
    private Integer specStorage;

    /**
     * 活动库存
     */
    @ApiModelProperty(value = "活动库存")
    private Integer specActivityStorage;
    /**
     * 属性值集合
     */
    @ApiModelProperty(value = "规格属性值集合")
    private List<SpecAttrValueDTO> specAttrValueDTOList;

}
