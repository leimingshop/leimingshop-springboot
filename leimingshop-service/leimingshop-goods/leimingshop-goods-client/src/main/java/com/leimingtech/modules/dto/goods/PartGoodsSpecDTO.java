/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.goods;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 功能描述：
 * 购物车专用商品规格实体
 *
 * @author 宋文豪
 * @email: songwenhao@leimingtech.com
 * @Date : 2020/3/19
 **/

@Data
@ApiModel(description = "PartGoodsSpecDTO")
public class PartGoodsSpecDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @ApiModelProperty(value = "ID")
    private Long id;
    /**
     * 商品ID
     */
    @ApiModelProperty(value = "商品ID")
    private Long goodsId;
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


    @ApiModelProperty(value = "商品重量")
    private Double specWeight;

    /**
     * 是否启用（默认0启用，1未启用）
     */
    @ApiModelProperty(value = "是否启用（默认0启用，1未启用）")
    private Integer isEnable;

    /**
     * 是否是主规格（默认0不是，1是）
     */
    @ApiModelProperty(value = "是否是主规格（默认0不是，1是）")
    private Integer mainFlag;

    /**
     * 规格上下架状态（默认:2未上架,0下架状态，1上架状态）
     */
    @ApiModelProperty(value = "规格上下架状态（默认:2未上架,0下架状态，1上架状态）")
    private Integer specShow;

    /**
     * 商品规格属性值名称
     */
    @ApiModelProperty(value = "商品规格属性值名称（中间用逗号隔开）")
    private String specAttrName;

    /**
     * 商品规格属性和属性值名称（key:value）
     */
    @ApiModelProperty(value = "商品规格属性和属性值名称（key:value）")
    private String specAttrValueName;

    /**
     * 商品规格主图
     */
    @ApiModelProperty(value = "商品规格主图")
    private String specMainPicture;

    @ApiModelProperty
    private List<SpecActivityDTO> specActivityList;


    @ApiModelProperty(value = "商品品牌id")
    private Long brandId;

    @ApiModelProperty(value = "店铺ID")
    private Long storeId;

}
