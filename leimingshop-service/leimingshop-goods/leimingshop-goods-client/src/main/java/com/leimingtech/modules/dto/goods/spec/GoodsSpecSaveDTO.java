/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.goods.spec;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: weixianchun
 * @Description: 商品规格管理(保存对象)
 * @Date :2019/6/4 17:42
 * @Version V1.0
 **/
@Data
@ApiModel(description = "GoodsSpecSaveDTO")
public class GoodsSpecSaveDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
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
     * 规格库存
     */
    @ApiModelProperty(value = "规格库存")
    private Integer specStorage;
    /**
     * 规格重量
     */
    @ApiModelProperty(value = "规格重量")
    private Double specWeight;

    /**
     * 商品规格主图
     */
    @ApiModelProperty(value = "商品规格主图")
    private String specMainPicture;

    @ApiModelProperty(value = "100*100图片地址")
    private String oneHundredPxPictureUrl;

    @ApiModelProperty(value = "200*200图片地址")
    private String twoHundredPxPictureUrl;

    @ApiModelProperty(value = "400*400图片地址")
    private String fourHundredPxPictureUrl;

    @ApiModelProperty(value = "800*800图片地址")
    private String eightHundredPxPictureUrl;
    /**
     * 是否是主规格（默认0不是，1是）
     */
    @ApiModelProperty(value = "是否是主规格（默认0不是，1是）")
    private Integer mainFlag;

    /**
     * 规格属性关系保存集合
     */
    @ApiModelProperty(value = "规格属性关系保存集合")
    private List<SpecAttributeRelationSaveDTO> specAttributeRelationSaveDTOList;


}
