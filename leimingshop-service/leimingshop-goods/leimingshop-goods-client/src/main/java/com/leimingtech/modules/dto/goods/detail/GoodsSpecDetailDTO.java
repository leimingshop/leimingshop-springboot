/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.goods.detail;

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
@ApiModel(description = "GoodsSpecDetailDTO")
public class GoodsSpecDetailDTO implements Serializable {

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
     * 规格主图
     */
    @ApiModelProperty(value = "规格主图")
    private String specMainPicture;

    /**
     * 商品规格属性值名称
     */
    @ApiModelProperty(value = "商品规格属性值名称（中间用逗号隔开）")
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

    /**
     * 规格重量
     */
    @ApiModelProperty(value = "规格重量")
    private Double specWeight;

    /**
     * 规格的名称和值
     */
    @ApiModelProperty(value = "规格的名称和值")
    private String specAttrValueName;
    /**
     * 规格属性值信息
     */
    @ApiModelProperty(value = "规格属性值信息")
    private List<SpecAttributeValueDetailDTO> specAttributeValueDetailDTOList;

}